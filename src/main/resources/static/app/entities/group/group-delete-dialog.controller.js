(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('GroupDeleteController',GroupDeleteController);

    GroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'Group'];

    function GroupDeleteController($uibModalInstance, entity, Group) {
        var vm = this;

        vm.group = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Group.delete(id)
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
