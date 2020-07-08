(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SequenceDeleteController',SequenceDeleteController);

    SequenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sequence'];

    function SequenceDeleteController($uibModalInstance, entity, Sequence) {
        var vm = this;

        vm.sequence = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sequence.delete(id)
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
