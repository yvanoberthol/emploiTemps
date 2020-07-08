(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentDeleteController',StudentDeleteController);

    StudentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Student'];

    function StudentDeleteController($uibModalInstance, entity, Student) {
        var vm = this;

        vm.student = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            if(vm.student.deletable){
                Student.delete(id)
                .then(function(response){
                    onSaveSuccess(response);
                }, function(){
                    onSaveError();
                });
            }
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
