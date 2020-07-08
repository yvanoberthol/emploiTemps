(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('MatiereDialogController', MatiereDialogController);

    MatiereDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance',  '$uibModal', 'entity', 'Matiere', 'Groupe'];

    function MatiereDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $uibModal, entity, Matiere, Groupe) {
        var vm = this;
        vm.matiere = entity;
        vm.matiere.promoId = $stateParams.promoId;

        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
         }

        function save () {
            vm.isSaving = true;
            if (vm.matiere.id !== null) {
                Matiere.update(vm.matiere, onSaveSuccess, onSaveError);
            } else {
                Matiere.save(vm.matiere, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $uibModalInstance.close(result);
            console.log(result)
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        Groupe.getByPromo($stateParams.promoId).then(function(response){
            vm.groupes = response.data;
        });

        vm.saveGroupe = function () {
            vm.isSaving = true;
            vm.groupe.promoId = $stateParams.promoId;
            Groupe.save(vm.groupe, onSaveSuccessGroupe, onSaveError);
        }

        function onSaveSuccessGroupe (result) {
            vm.groupes.push(result.data);
            vm.matiere.groupeId = result.data.id;
            vm.groupe = null;
            vm.modalInstance2.close();
            vm.isSaving = false;
        }

        vm.clear2 = clear2;
        function clear2 () {
            vm.modalInstance2.dismiss();
        }
        vm.addGroupe = addGroupe;
        function addGroupe(){
            vm.modalInstance2 = $uibModal.open({
                templateUrl: 'app/entities/groupe/groupe-dialog.html',
                scope: $scope,
                size: 'md'
            });
        };

    }
})();
