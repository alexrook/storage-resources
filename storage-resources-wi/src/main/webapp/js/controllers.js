'use strict';

/* Controllers */

angular.module('sres.controllers', []).
        controller('UserCtrl',
                ['$scope', '$http',
                    function($scope, $http) {

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
        controller('ReportCtrl',
                ['$scope', '$http','$routeParams',
                    function($scope, $http,$routeParams) {
                        
                        console.log($routeParams);
                        $scope.report={};
                        $scope.user=$routeParams.user;
                        window.user=$routeParams.user;
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
                             $scope.report=ret;
                        });
                        
                    }]);
        
        