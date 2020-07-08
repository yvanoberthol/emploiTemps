(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeDeleteController',AnneeDeleteController);

    AnneeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Annee'];

    function AnneeDeleteController($uibModalInstance, entity, Annee) {
        var vm = this;

        vm.annee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Annee.delete(id)
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
