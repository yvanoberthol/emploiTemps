(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('TrimestreDetailController', TrimestreDetailController);

    TrimestreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity'];

    function TrimestreDetailController($scope, $rootScope, $stateParams, previousState, entity) {
        var vm = this;
        vm.trimestre = entity;

        vm.previousState = previousState.name;

    }
})();
