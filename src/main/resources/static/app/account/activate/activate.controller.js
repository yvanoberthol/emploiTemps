(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ActivateController', ActivateController);

    ActivateController.$inject = ['$stateParams', '$http'];

    function ActivateController ($stateParams, $http) {
        var vm = this;

        //console.log("activate")
        //console.log($stateParams.key);

        $http.get("/api/activate?key=" + $stateParams.key)
        .then(function successCallback(response) {
            vm.error = null;
            vm.success = 'OK';
        },
        function errorCallback(response) {
            vm.success = null;
            vm.error = 'ERROR';
        });
    }
})();
