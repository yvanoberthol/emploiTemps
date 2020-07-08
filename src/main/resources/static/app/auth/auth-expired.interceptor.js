(function() {
    'use strict';

    angular
        .module('myApp')
        .factory('authExpiredInterceptor', authExpiredInterceptor);

    authExpiredInterceptor.$inject = ['$rootScope', '$q', '$injector', 'AUTH_EVENTS'];

    function authExpiredInterceptor($rootScope, $q, $injector, AUTH_EVENTS) {
        var service = {
            responseError: responseError
        };
        return service;

        function responseError(response) {
            // If we have an unauthorized request we redirect to the login page
            // Don't do this check on the account API to avoid infinite loop
            //if (response.status === 401 && angular.isDefined(response.data.path) && response.data.path.indexOf('/api/account') === -1) {
            if (response.status === 401 || response.status === 403) {
                var auth = $injector.get('auth');
                var to = $rootScope.toState;
                var params = $rootScope.toStateParams;
                auth.logout();
                /*
                if (to.name !== 'accessdenied') {
                    auth.storePreviousState(to.name, params);
                }
                var LoginService = $injector.get('LoginService');
                LoginService.open();
                */
                //console.log(response);
                $rootScope.$broadcast({
                    401: AUTH_EVENTS.notAuthenticated,
                    403: AUTH_EVENTS.notAuthorized
                }[response.status], response);
            }
            return $q.reject(response);
        }
    }
})();
