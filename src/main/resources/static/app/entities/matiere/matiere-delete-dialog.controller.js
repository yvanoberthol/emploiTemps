(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('MatiereDeleteDialogController',MatiereDeleteDialogController);

    MatiereDeleteDialogController.$inject = ['$uibModalInstance', 'entity', 'Matiere'];

    function MatiereDeleteDialogController($uibModalInstance, entity, Matiere) {
        var vm = this;
        console.log("test")
        vm.matiere = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Matiere.delete(id)
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
