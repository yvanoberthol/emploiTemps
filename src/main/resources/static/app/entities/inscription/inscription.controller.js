(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('InscriptionController', InscriptionController);

    InscriptionController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'Inscription', '$filter', 'NgTableParams', 'Promo', '$uibModal','SmsDialogService'];

    function InscriptionController ($rootScope, $scope, $stateParams, session, $state, $http,
                                Inscription, $filter, NgTableParams, Promo, $uibModal, SmsDialogService) {
        var vm = this;
        vm.loading = true;
        vm.sendSms = SmsDialogService.open;
        vm.sendSmsGroup = SmsDialogService.openGroup;
        vm.selected = [];
        vm.inscriptions = [];
        vm.count = "10";

        var patternDateTime = "YYYY-MM-DD HH:mm";
        var patternDate = "YYYY-MM-DD";
        vm.name = "";
        vm.parent = "";
        vm.phone = "";
        vm.promoId = 0;

        if($stateParams.promoId)
        vm.promoId = $stateParams.promoId;

        Promo.getByAnnee(session.getAnneeId())
            .then(function(response){
                vm.promos = response.data;
        })

        var initialParams = {
            count: 200, // initial page size
            sorting: { 'student': "asc" }
        };

        vm.tableParams = new NgTableParams(initialParams, {
            counts: [],
            paginationMaxBlocks: 10,
            paginationMinBlocks: 2,

            getData: function(params) {
                if(params.sorting()){
                    for(var sortBy in params.sorting()){
                        vm.sortBy = sortBy;
                        vm.direction = params.sorting()[sortBy];
                    }
                }

                if(!vm.promoId)
                vm.promoId = 0;
                return Inscription.getByAnnee(params.page()-1, params.count(), vm.sortBy, vm.direction, session.getAnneeId(), vm.name, vm.promoId)
                .then(function(response){
                    params.total(response.data.totalElements);
                    vm.inscriptions = response.data.content;
                    vm.inscriptions.forEach(function(inscription){
                        if(inscription.student.dateNaissance)
                        inscription.student.dateNaissance =  moment(inscription.student.dateNaissance, "DDMMYYYY").format("DD/MM/YYYY");
                    });
                    vm.loading = false;
                    return vm.inscriptions;
                })
            }

        });

        vm.promoChanged = function(){
            vm.promo =  $filter('filter')(vm.promos, {id: vm.promoId})[0];
            vm.tableParams.reload();
        }

        function existInArray(item, array){
            for (var i=0; i < array.length; ++i) {
                if (array[i].student.id == item.student.id) {
                    return true;
                    break;
                }
            }
            return false;
        }

        vm.isSelected = function(inscription){
            return existInArray(inscription, vm.selected)
        }

        function indexOf(item, array){
            for (var i=0; i < array.length; ++i) {
                if (array[i].student.id == item.student.id) {
                    return i;
                    break;
                }
            }
            return -1;
        }
        function remove(array, element) {
          const index = indexOf(element, array);
          if(index != -1)
          array.splice(index, 1);
        }

        vm.toggleInscription = function(inscription) {
          if(inscription.selected)
              vm.selected.push(inscription)
          else
              remove(vm.selected, inscription)
          vm.allSelected = vm.isChecked();
        };

        vm.isChecked = function() {
            for (var i = 0; i < vm.inscriptions.length; i++) {
              if (!vm.inscriptions[i].selected)
                return false
            }
            return true;
        };

        vm.toggleAll = function() {
            if (!vm.allSelected) {
              for (var i = 0; i < vm.inscriptions.length; i++) {
                vm.inscriptions[i].selected = false;
                remove(vm.selected, vm.inscriptions[i])
              }
            }
            else {
              for (var i = 0; i < vm.inscriptions.length; i++) {
                if(!vm.inscriptions[i].selected){
                    vm.inscriptions[i].selected = true;
                    vm.selected.push(vm.inscriptions[i])
                }
              }
            }
        };

        vm.search = search;
        function search() {
            vm.tableParams.page(1);
            vm.tableParams.count(vm.count);
            vm.tableParams.reload();
        }

    }
})();

