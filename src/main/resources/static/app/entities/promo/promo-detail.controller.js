(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoDetailController', PromoDetailController);

    PromoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', '$state', 'previousState', 'entity'];

    function PromoDetailController($scope, $rootScope, $stateParams, $state, previousState, entity) {
        var vm = this;
        vm.promo = entity;

        vm.previousState = previousState.name;
        //vm.selected = 0;
        //$state.go('promo-detail.students');
    }
})();
