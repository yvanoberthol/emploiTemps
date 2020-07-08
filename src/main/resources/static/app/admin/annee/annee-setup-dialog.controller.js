(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeSetupDialogController', AnneeSetupDialogController);

    AnneeSetupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Annee',
                                            'Promo', 'NgTableParams'];

    function AnneeSetupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Annee,
                                        Promo, NgTableParams) {
        var vm = this;
        vm.annee = entity;
        console.log(vm.annee);
        vm.loading = true;

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
            if (vm.annee.id !== null) {
                Annee.update(vm.annee, onSaveSuccess, onSaveError);
            } else {
                Annee.save(vm.annee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:anneeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function existItemNotEnabled(items){
            for (var i = 0; i < items.length; i++) {
                if(!items[i].enabled)
                    return true;
            }
            return false;
        }

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'name': "asc" }
        };

        Promo.getByAnnee(vm.annee.id)
            .then(function(response){
                vm.loading = false;
                vm.promos = response.data;
                console.log(vm.promos);
                /*vm.selectAllClasses = true;
                if(existItemNotEnabled(vm.promos))
                    vm.selectAllClasses = false;*/
                vm.tableParams = new NgTableParams(initialParams, {
                    counts: [],
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.promos
                });
        })

        vm.updateEnabled = function(promo){
            promo.enabled = !promo.enabled;
            Promo.update(promo);
        }

        vm.togglePromo = function(promo){
            if (!promo.enabled) {
                vm.selectAllClasses = false;
            }
            else{
                if(!existItemNotEnabled(vm.promos))
                    vm.selectAllClasses = true;
            }
            Promo.update(promo);
        }

        vm.toggleAllClasses = function() {
          var checked = vm.selectAllClasses;
          vm.promos.forEach(function(promo){
            promo.enabled = checked;
            Promo.update(promo);
          })
        };
    }
})();
