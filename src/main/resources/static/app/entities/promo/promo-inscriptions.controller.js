(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoInscriptionsController', PromoInscriptionsController);

    PromoInscriptionsController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity',
                                        'Inscription', '$filter', 'NgTableParams', 'DownloadService'];

    function PromoInscriptionsController($scope, $rootScope, $stateParams, previousState, entity,
                                        Inscription, $filter, NgTableParams, DownloadService) {
        var vm = this;
        vm.promo = entity;
        vm.previousState = previousState.name;
        vm.name = "";

        var initialParamsPromos = {
            count: 20, // initial page size
            //sorting: { 'name': "asc" }
        };

        Inscription.getByPromo(vm.promo.id, vm.name)
            .then(function(response){
                vm.loading = false;
                vm.inscriptions = response.data;
                vm.tableParams = new NgTableParams(initialParamsPromos, {
                    counts: [],
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.inscriptions
                });
        })

        vm.openStudentCards = function(){
           DownloadService.openStudentCards(vm.promo.id);
        }

    }
})();
