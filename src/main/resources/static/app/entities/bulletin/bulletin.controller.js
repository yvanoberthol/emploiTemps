(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('BulletinController', BulletinController);

    BulletinController.$inject = ['$rootScope', '$scope', '$http', 'auth', 'session', '$uibModalInstance',
                                 'bulletin', '$filter'];

    function BulletinController ($rootScope, $scope, $http, auth, session, $uibModalInstance,
                                 bulletin, $filter) {
        var vm = this;
        vm.clear = clear;
        //vm.bulletin = bulletin;
        vm.bulletin = new Uint8Array(bulletin);

        function clear () {
           $uibModalInstance.dismiss();
        }
    }
})();
