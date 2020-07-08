(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeDetailController', AnneeDetailController);

    AnneeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Annee',
                                    'Promo', 'NgTableParams'];

    function AnneeDetailController($scope, $rootScope, $stateParams, previousState, entity, Annee,
                                    Promo, NgTableParams) {
        var vm = this;
        vm.annee = entity;

        vm.previousState = previousState.name;

        console.log(vm.annee);
        vm.loading = true;

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'name': "asc" }
        };

        Promo.getByAnnee(vm.annee.id)
            .then(function(response){
                vm.loading = false;
                vm.promos = response.data;
                console.log(vm.promos);
                vm.tableParams = new NgTableParams(initialParams, {
                    counts: [],
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.promos
                });
        })

        vm.updateEnabled = function(promo){
            promo.enabled = !promo.enabled;
            Promo.update(promo);
        }

    }
})();
