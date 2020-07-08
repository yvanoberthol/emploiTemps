(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['$translate', '$scope', '$http', '$location', '$state'];

    function RegisterController ($translate, $scope, $http, $location, $state) {
        var vm = this;
        //vm.login = LoginService.open;
        vm.login = login;
        vm.requestResetPassword = requestResetPassword;

        vm.user = {};
        //vm.user.langKey = "fr";
        vm.user.langKey = $translate.use();

       vm.register = function(){
            vm.isSaving = true;
            vm.user.login = vm.user.email;
            vm.user.familyId = 1;
            if (vm.user.password !== vm.confirmPassword) {
               vm.doNotMatch = 'ERROR';
               vm.isSaving = false;
            }
            else{
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                $http.post("/register", vm.user)
                .then(function successCallback(response) {
                    //console.log(response.data);
                    //$location.path("/login");
                    vm.success = 'OK';
                    vm.error = false;
                    vm.isSaving = false;
                },
                function errorCallback(response) {
                    vm.isSaving = false;

                    if(response.data && response.data.error == "e-mail address already in use")
                    vm.errorEmailExists = true;

                    else if(response.data && response.data.error == "login already in use")
                    vm.errorUserExists = true;

                    else
                    vm.error = true;
                });
            }
       };

       function login () {
           $state.go('login');
       }

       function requestResetPassword () {
           $state.go('requestReset');
       }
    }
})();
