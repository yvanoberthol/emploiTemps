(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentCardDetailController', StudentCardDetailController);

    StudentCardDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity'];

    function StudentCardDetailController($scope, $rootScope, $stateParams, previousState, entity) {
        var vm = this;
        vm.studentCard = entity;

        vm.previousState = previousState.name;

    }
})();
