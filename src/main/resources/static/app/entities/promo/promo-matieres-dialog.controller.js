(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoMatieresDialogController', PromoMatieresDialogController);

    PromoMatieresDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Promo',
                                            'Matiere', 'NgTableParams', 'PromoMatiere', '$uibModal', 'EdtEvent'];

    function PromoMatieresDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Promo,
                                        Matiere, NgTableParams, PromoMatiere, $uibModal, EdtEvent) {
        var vm = this;
        vm.promo = entity;
        console.log(vm.promo);
        vm.loading = true;

        vm.clear = clear;
        function clear () {
            $uibModalInstance.dismiss();
        }

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'name': "asc" }
        };

        var matieres = [];
        vm.promo.promoMatieres.forEach(function(matiere){
            if(matiere.enabled)
            matieres.push(matiere);
        })

        vm.tableParams = new NgTableParams(initialParams, {
            counts: [],
            paginationMaxBlocks: 10,
            paginationMinBlocks: 2,
            dataset: matieres
        });

        vm.updateEnabled = function(promoMatiere){
            promoMatiere.enabled = !promoMatiere.enabled;
        }

        vm.openCahierTexteModal = openCahierTexteModal;
        function openCahierTexteModal(promoMatiere){
            vm.promoMatiere = promoMatiere;

            EdtEvent.getByPromoMatiere(promoMatiere.promoId, promoMatiere.matiereId)
            .then(function(response){
                vm.events = response.data;
            })
            vm.cahierTexteModalInstance = $uibModal.open({
                templateUrl: 'app/entities/promo/promo-matieres-dialog-cahier-texte-modal.html',
                scope: $scope,
                backdrop: false,
                size: 'lg'
            });
        };

        vm.clear2 = clear2;
        function clear2 () {
            if(vm.cahierTexteModalInstance)
                vm.cahierTexteModalInstance.dismiss();
        }
    }
})();
