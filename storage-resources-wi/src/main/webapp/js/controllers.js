'use strict';

/* Controllers */

angular.module('sres.controllers', []).
        controller('IdxCtrl',
                ['$scope', 'shared',
                    function($scope, shared) {
                        shared.addListener(function(data){
                            $scope.reportTypes=shared.getReportTypes();
                            
                        },'reporttypes');
                        shared.addListener(function(data){
                            $scope.user=shared.getUser();
                        },'user');
                    }]).
        controller('UserCtrl',
                ['$scope', '$http','shared',
                    function($scope, $http,shared) {

                        $scope.users = [];

                        var url='',method='';
                        if (window.urlBase) {
                            url=window.urlBase+'/'+'rest/users';
                            //method='jsonp'
                        } else {
                            url='rest/users';
                            //method='get';
                        }
                        
                        $http.get(url).success(function(data) {
                            var users = data.users ? data.users : data;
                            for (var i = 0; i < users.length; i++) {
                                users[i].id = i+1;
                               }
                               
                               $scope.users=users;
                        });
                    }]).
        controller('ReportWrapCtrl',
                ['$scope', '$http','$routeParams','shared',
                    function($scope, $http,$routeParams,shared) {
                        
                        console.log($routeParams);
                        
                        shared.addListener(function(){
                            $scope.report=shared.getReport();
                        },'report');
                        
                        var reportTypes=[{ //todo: $http.get(...)...
                                name:"Большие файлы",
                                type:"bigfiles"
                                },
                            {
                                name:"Файлы по группам",
                                type:"bytypefiles"
                                }/*,
                            {
                                name:"Дубликаты файлов",
                                type:"duplicatefiles"
                            }*/];
                        
                        shared.setReportTypes(reportTypes);
                        shared.setUser($routeParams.user);
                        
                        if (!$routeParams.reporttype) {
                            $routeParams.reporttype=reportTypes[0].type;
                        }
                        $scope.reportBodyInc='partials/reports/'
                                        +$routeParams.reporttype+'.html';
                        
                        
                    }]).
        controller('ReportBodyCtrl',
                ['$scope', '$http','$routeParams','shared',
                    function($scope, $http,$routeParams,shared) {
                        
                        console.log($routeParams);
                        $scope.report={};
                        $scope.user=$routeParams.user;
                        var url='',method='';
                        if (window.urlBase) {
                            url=window.urlBase+'/'+'rest/reports';
                        } else {
                            url='rest/reports';
                        }
                        
                        function cleanup(report){
                            if (report._type==='ByTypeFiles') {
                                for (var i=0;i<report.groups.group.length;i++){
                                    if (report.groups.group[i].files) {
                                        if (!angular.isArray(report.groups.group[i].files.file))
                                        {
                                            report.groups.group[i].files.file=
                                            new Array(
                                                    report.groups.group[i].files.file
                                                    );
                                        }
                                    }
                                }
                            }
                            
                            return report;
                        }
                        
                        $http.get(url,{params:$routeParams}).success(function(data) {
                             var x2js = new X2JS();
                             var jsonObj = x2js.xml_str2json(data);
                             console.log(jsonObj);
                             var ret=cleanup(jsonObj.report);
                             shared.setReport(ret);
                             $scope.report=ret;
                        });
                        
                    }]);
        
        