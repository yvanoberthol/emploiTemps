(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ReglementDeleteController',ReglementDeleteController);

    ReglementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Reglement'];

    function ReglementDeleteController($uibModalInstance, entity, Reglement) {
        var vm = this;

        vm.reglement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Reglement.delete(id)
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
