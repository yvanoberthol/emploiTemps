(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SmsController', SmsController);

    SmsController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'Sms', '$filter', 'NgTableParams'];

    function SmsController ($rootScope, $scope, $stateParams, session, $state, $http,
                                Sms, $filter, NgTableParams) {
        var vm = this;
        vm.loading = true;

        var patternDateTime = "YYYY-MM-DD HH:mm";
        var patternDate = "YYYY-MM-DD";
        vm.sender = "";
        vm.recipient = "";
        vm.count = "10";

        var patternDateTime = "YYYY-MM-DD HH:mm";
        var patternDate = "YYYY-MM-DD";

        var fromLabel = "Du";
        var toLabel = "Au";
        var applyLabel = "Valider";
        var cancelLabel = "Annuler";
        var customRangeLabel = "Autre";
        var todayLabel = "Aujourd'hui";
        var yesterdayLabel = "Hier";
        var thisWeekLabel = "Cette Semaine";
        var thisMonthLabel = "Ce Mois";
        var thisYearLabel = "Cette Année";
        var daysOfWeek =  ["Di", "Lu", "Ma", "Me", "Je", "Ve", "Sa" ];
        var monthNames = [ "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin",
                        "Julliet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre" ]

        //default range
        vm.range = {startDate: moment().startOf('year'), endDate: moment().endOf('year')};

        var ranges = {};
        //ranges[todayLabel] = [moment().startOf('day'), moment().endOf('day')];
        //ranges[yesterdayLabel] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
        ranges[thisWeekLabel] = [moment().startOf('isoweek'), moment().endOf('isoweek')];
        ranges[thisMonthLabel] = [moment().startOf('month'), moment().endOf('month')];
        ranges[thisYearLabel] = [moment().startOf('year'), moment().endOf('year')];

        vm.options = {
          applyClass: 'btn-success',
          locale: {
            applyLabel: applyLabel,
            fromLabel: fromLabel,
            format: "DD/MM/YYYY", //will give you 2017-01-06
            toLabel: toLabel,
            cancelLabel: cancelLabel,
            customRangeLabel: customRangeLabel,
            "daysOfWeek": daysOfWeek,
            "monthNames": monthNames,
            "firstDay": 1
          },
          ranges: ranges,
          eventHandlers: {
            'hide.daterangepicker': function() {
                vm.search();
            }
          }
        }

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'createdDate': "desc" }
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

                return  Sms.getAll(params.page()-1, params.count(), vm.sortBy, vm.direction,
                                vm.dateTimeFrom, vm.dateTimeTo, vm.sender, vm.recipient)
                .then(function(response){
                    params.total(response.data.totalElements);
                    vm.smss = response.data.content;
                    vm.loading = false;
                    return vm.smss;
                })
            }
        });


        vm.search = search;
        function search() {
            vm.dateFrom = vm.range.startDate.format(patternDate);
            vm.dateTo = vm.range.endDate.format(patternDate);
            vm.dateTimeFrom = vm.range.startDate.format(patternDateTime);
            vm.dateTimeTo = vm.range.endDate.format(patternDateTime);

            vm.tableParams.page(1);
            vm.tableParams.count(vm.count);
            vm.tableParams.reload();
        }

        vm.search();
    }
})();

