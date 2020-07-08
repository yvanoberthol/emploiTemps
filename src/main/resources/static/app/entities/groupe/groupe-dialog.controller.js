(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('GroupeDialogController', GroupeDialogController);

    GroupeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Groupe'];

    function GroupeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Groupe) {
        var vm = this;
        vm.groupe = entity;
        vm.groupe.promoId = $stateParams.promoId;

        vm.clear2 = clear;
        vm.saveGroupe = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.groupe.id !== null) {
                Groupe.update(vm.groupe, onSaveSuccess, onSaveError);
            } else {
                Groupe.save(vm.groupe, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
