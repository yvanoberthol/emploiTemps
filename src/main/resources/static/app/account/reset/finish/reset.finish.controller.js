(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ResetFinishController', ResetFinishController);

    ResetFinishController.$inject = ['$stateParams', '$timeout', '$http'];

    function ResetFinishController ($stateParams, $timeout, $http) {
        var vm = this;

        vm.keyMissing = angular.isUndefined($stateParams.key);
        vm.confirmPassword = null;
        vm.doNotMatch = null;
        vm.error = null;
        vm.finishReset = finishReset;
        vm.resetAccount = {};
        vm.success = null;

        $timeout(function (){angular.element('#password').focus();});

        function finishReset() {
            vm.doNotMatch = null;
            vm.error = null;
            if (vm.resetAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                $http.post("/api/account/reset_password/finish", {key: $stateParams.key, newPassword: vm.resetAccount.password})
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