(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentCardDeleteController',StudentCardDeleteController);

    StudentCardDeleteController.$inject = ['$uibModalInstance', 'entity', 'StudentCard'];

    function StudentCardDeleteController($uibModalInstance, entity, StudentCard) {
        var vm = this;

        vm.studentCard = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StudentCard.delete(id)
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
