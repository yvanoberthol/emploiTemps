(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('GroupeDeleteDialogController',GroupeDeleteDialogController);

    GroupeDeleteDialogController.$inject = ['$uibModalInstance', 'entity', 'Groupe'];

    function GroupeDeleteDialogController($uibModalInstance, entity, Groupe) {
        var vm = this;
        vm.groupe = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Groupe.delete(id)
            .then(function(response){
                onSaveSuccess(response);
            }, function(){
                onSaveError();
            });
        }

        function onSaveSuccess (response) {
            $uibModalInstance.close(response);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
