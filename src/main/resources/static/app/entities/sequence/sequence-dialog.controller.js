(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SequenceDialogController', SequenceDialogController);

    SequenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sequence'];

    function SequenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sequence) {
        var vm = this;
        vm.sequence = entity;
        console.log(vm.sequence);

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
            if (vm.sequence.id !== null) {
                Sequence.update(vm.sequence, onSaveSuccess, onSaveError);
            } else {
                Sequence.save(vm.sequence, onSaveSuccess, onSaveError);
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
