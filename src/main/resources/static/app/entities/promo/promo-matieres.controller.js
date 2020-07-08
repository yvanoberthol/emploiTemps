(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoMatieresController', PromoMatieresController);

    PromoMatieresController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity',
                                        'Matiere', '$filter', 'NgTableParams'];

    function PromoMatieresController($scope, $rootScope, $stateParams, previousState, entity,
                                        Matiere, $filter, NgTableParams) {
        var vm = this;
        vm.promo = entity;
        vm.previousState = previousState.name;

        var initialParamsPromos = {
            count: 20, // initial page size
            //sorting: { 'name': "asc" }
        };

        Matiere.getByPromo(vm.promo.id)
            .then(function(response){
                vm.loading = false;
                vm.matieres = response.data;
                vm.tableParams = new NgTableParams(initialParamsPromos, {
                    counts: [],
                    // page size buttons (right set of buttons in demo)
                    //counts: [10,25,50,100],
                    //filterDelay: 1000,
                    // determines the pager buttons (left set of buttons in demo)
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.matieres
                });
        })

    }
})();
