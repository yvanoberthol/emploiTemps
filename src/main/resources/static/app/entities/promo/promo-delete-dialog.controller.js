(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoDeleteController',PromoDeleteController);

    PromoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Promo'];

    function PromoDeleteController($uibModalInstance, entity, Promo) {
        var vm = this;

        vm.promo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Promo.delete(id)
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
