(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PasswordController', PasswordController);

    PasswordController.$inject = ['auth', '$http'];

    function PasswordController (auth, $http) {
        var vm = this;
        vm.user = null;
        auth.getUser().then(function(user){
            vm.user = user;
        });

        vm.changePassword = changePassword;
        vm.doNotMatch = null;
        vm.error = null;
        vm.success = null;


        function changePassword () {
            if (vm.password !== vm.confirmPassword) {
                vm.error = null;
                vm.success = null;
                vm.doNotMatch = 'ERROR';
            } else {
                vm.doNotMatch = null;

                $http.post("/api/account/change-password", vm.password)
                .then(function(data){
                     vm.error = null;
                     vm.success = 'OK';
                }, function() {
                      vm.success = null;
                      vm.error = 'ERROR';
               });
            }
        }
    }
})();
