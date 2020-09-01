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
            count: 100, // initial page size
            sorting: { 'student.lastName': "asc" }
        };

        Inscription.getByPromo(vm.promo.id, vm.name)
            .then(function(response){
                vm.loading = false;
                vm.inscriptions = response.data;
                vm.inscriptions.forEach(function(inscription){
                    if(inscription.student.dateNaissance)
                    inscription.student.dateNaissance =  moment(inscription.student.dateNaissance, "DDMMYYYY").format("DD/MM/YYYY");
                });
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

        vm.openListeDefinitive = function(){
           DownloadService.openListeDefinitive(vm.promo.id);
        }

    }
})();
