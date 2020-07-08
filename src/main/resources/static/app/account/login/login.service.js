(function() {
    'use strict';

    angular
        .module('myApp')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$uibModal', '$rootScope'];

    function LoginService ($uibModal, $rootScope) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };
        return service;

        function open () {
            if (modalInstance !== null) return;
            console.log("login open")

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/account/login/login.html',
                controller: 'LoginController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'md',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('login');
                        return $translate.refresh();
                    }]
                }
            });

            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();
