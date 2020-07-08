(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('TrimestreDeleteController',TrimestreDeleteController);

    TrimestreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Trimestre'];

    function TrimestreDeleteController($uibModalInstance, entity, Trimestre) {
        var vm = this;

        vm.trimestre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Trimestre.delete(id)
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
