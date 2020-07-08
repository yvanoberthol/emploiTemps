(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ContactController', ContactController);

    ContactController.$inject = ['$scope', '$state', 'session', '$stateParams', 'Contact', 'NgTableParams', 'SmsDialogService'];

    function ContactController ($scope, $state, session, $stateParams, Contact, NgTableParams, SmsDialogService) {
        var vm = this;
        vm.loading = true;
        vm.sendSms = SmsDialogService.open;
        vm.sendSmsGroup = SmsDialogService.openGroup;
        vm.selected = [];

        vm.contacts = [];
        vm.sortBy = "";
        vm.direction = "";
        vm.count = "10";
        vm.name = "";
        vm.phone = "";
        vm.group = "";

        if($stateParams.group)
        vm.group = $stateParams.group;

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'createdDate': "desc" }
        };

        vm.tableParams = new NgTableParams(initialParams, {
             // page size buttons (right set of buttons in demo)
             //counts: [10,25,50,100],
             counts: [],

             //filterDelay: 1000,
             // determines the pager buttons (left set of buttons in demo)
             paginationMaxBlocks: 10,
             paginationMinBlocks: 2,

             getData: function(params) {
             //vm.loading = true;

             if(params.sorting()){
                 //console.log(params.sorting());
                 for(var sortBy in params.sorting()){
                   vm.sortBy = sortBy;
                   vm.direction = params.sorting()[sortBy];
                 }
             }

             return Contact.getAll( params.page()-1,        //page
                                    params.count(),         //size
                                    vm.sortBy,              //sortBy
                                    vm.direction,
                                    vm.name,
                                    vm.phone,
                                    vm.group)
             .then(function(response){
                   params.total(response.data.totalElements);
                   //vm.loading = false;
                   //return response.data.content;
                   vm.contacts = response.data.content;
                   vm.contacts.forEach(function(contact){
                    contact.selected = existInArray(contact, vm.selected)
                   })
                   vm.allSelected = vm.isChecked();
                   vm.loading = false;
                   return vm.contacts;
             })
           }
        });

        function existInArray(item, array){
            for (var i=0; i < array.length; ++i) {
                if (array[i].id == item.id) {
                    return true;
                    break;
                }
            }
            return false;
        }

        vm.isSelected = function(contact){
            return existInArray(contact, vm.selected)
        }

        function indexOf(item, array){
            for (var i=0; i < array.length; ++i) {
                if (array[i].id == item.id) {
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

        vm.toggleContact = function(contact) {
          if(contact.selected)
              vm.selected.push(contact)
          else
              remove(vm.selected, contact)
          vm.allSelected = vm.isChecked();
        };

        vm.isChecked = function() {
            for (var i = 0; i < vm.contacts.length; i++) {
              if (!vm.contacts[i].selected)
                return false
            }
            return true;
        };

        vm.toggleAll = function() {
            if (!vm.allSelected) {
              for (var i = 0; i < vm.contacts.length; i++) {
                vm.contacts[i].selected = false;
                remove(vm.selected, vm.contacts[i])
              }
            }
            else {
              for (var i = 0; i < vm.contacts.length; i++) {
                if(!vm.contacts[i].selected){
                    vm.contacts[i].selected = true;
                    vm.selected.push(vm.contacts[i])
                }
              }
            }
        };

        vm.search = search;
        function search(group) {
            if(group)
            vm.group = group;
            vm.tableParams.page(1);
            vm.tableParams.count(vm.count);
            vm.tableParams.reload();
        }
    }
})();
