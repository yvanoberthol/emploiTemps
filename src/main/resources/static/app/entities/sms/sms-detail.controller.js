(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SmsDetailController', SmsDetailController);

    SmsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity',
                                    'session'];

    function SmsDetailController($scope, $rootScope, $stateParams, previousState, entity,
                                    session) {
        var vm = this;
        vm.sms = entity;

        vm.previousState = previousState.name;
        vm.selected = 0;
        vm.selectedTab = 0;

        vm.tabSelected = function(index){
            vm.selectedTab = index;
        }


        vm.tabSelected(0);
    }
})();
