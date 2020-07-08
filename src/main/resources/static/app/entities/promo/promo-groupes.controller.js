(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoGroupesController', PromoGroupesController);

    PromoGroupesController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity',
                                        'Groupe', '$filter', 'NgTableParams'];

    function PromoGroupesController($scope, $rootScope, $stateParams, previousState, entity,
                                        Groupe, $filter, NgTableParams) {
        var vm = this;
        vm.promo = entity;
        vm.previousState = previousState.name;

        var initialParamsPromos = {
            count: 10, // initial page size
            sorting: { 'coef': "desc" }
        };

        Groupe.getByPromo(vm.promo.id)
            .then(function(response){
                vm.loading = false;
                vm.groupes = response.data;
                vm.tableParams = new NgTableParams(initialParamsPromos, {
                    counts: [],
                    // page size buttons (right set of buttons in demo)
                    //counts: [10,25,50,100],
                    //filterDelay: 1000,
                    // determines the pager buttons (left set of buttons in demo)
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.groupes
                });
        })

    }
})();
