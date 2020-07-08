(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('RequestResetController', RequestResetController);

    RequestResetController.$inject = ['$timeout', '$http', '$state'];

    function RequestResetController ($timeout, $http, $state) {
        var vm = this;

        vm.login = login;
        vm.register = register;

        vm.error = null;
        vm.errorEmailNotExists = null;
        vm.requestReset = requestReset;
        vm.resetAccount = {};
        vm.success = null;

        $timeout(function (){angular.element('#email').focus();});

        function requestReset () {
            vm.error = null;
            vm.errorEmailNotExists = null;

            $http.post("/api/account/reset_password/init", vm.resetAccount.email)
            .then(function(data){
                 vm.error = null;
                 vm.success = 'OK';
            }, function(response) {
                 vm.success = null;
                 if (response.status === 400 && response.data === 'e-mail address not registered') {
                     vm.errorEmailNotExists = 'ERROR';
                 } else {
                     vm.error = 'ERROR';
                }
            });
        }

        function login () {
           $state.go('login');
        }

        function register () {
           $state.go('register');
        }
    }
})();
