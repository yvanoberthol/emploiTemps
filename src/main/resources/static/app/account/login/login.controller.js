(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$scope', '$http', 'auth', 'session', '$state'];

    function LoginController ($rootScope, $scope, $http, auth, session, $state) {
        var vm = this;
        //vm.clear = clear;
        vm.login = login;
        vm.register = register;
        vm.requestResetPassword = requestResetPassword;

        console.log("login");

        /*function clear () {
           $uibModalInstance.dismiss();
        }*/

        function login(){
            vm.isSaving = true;
            vm.userNotActivated = false;
            vm.authenticationError = false;

            auth.login(vm.credentials)
            .then(function(response) {
                if(response == "OK"){
                    //clear();
                    $state.go('home', {}, {reload: true});
                }
                else{
                    console.log(response)
                    if(response == "Bad credentials")
                        vm.authenticationError = true;
                    else if(response == "User is disabled")
                        vm.userNotActivated = true;
                    else if(response == "User account has expired")
                        vm.accountExpired = true;
                    else
                    vm.authenticationError = true;
                }
                vm.isSaving = false;
            }, function(error){
                console.log(error)
            });
        };

        function register () {
            $state.go('register');
        }

        function requestResetPassword () {
            $state.go('requestReset');
        }
    }
})();
