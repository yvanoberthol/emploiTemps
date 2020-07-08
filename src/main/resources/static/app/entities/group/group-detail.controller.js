(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('GroupDetailController', GroupDetailController);

    GroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity'];

    function GroupDetailController($scope, $rootScope, $stateParams, previousState, entity) {
        var vm = this;
        vm.group = entity;

        vm.previousState = previousState.name;

    }
})();
