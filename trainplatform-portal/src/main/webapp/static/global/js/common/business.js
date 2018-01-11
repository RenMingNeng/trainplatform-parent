var business_tree = [ {
    "id" : 1,
    "open" : true,
    "name" : "全部行业",
    "children" : [ {
        "id" : 2,
        "open" : false,
        "name" : "电力行业",
        "children" : [ {
            "id" : 5,
            "open" : false,
            "name" : "发电",
            "children" : [ {
                "id" : 6,
                "open" : false,
                "name" : "火力发电",
                "pid" : 5
            }, {
                "id" : 7,
                "open" : false,
                "name" : "水力发电",
                "pid" : 5
            }, {
                "id" : 8,
                "open" : false,
                "name" : "风力发电",
                "pid" : 5
            }, {
                "id" : 9,
                "open" : false,
                "name" : "光伏发电",
                "pid" : 5
            }, {
                "id" : 10,
                "open" : false,
                "name" : "燃气发电",
                "pid" : 5
            }, {
                "id" : 11,
                "open" : false,
                "name" : "其他发电",
                "pid" : 5
            } ],
            "pid" : 2
        }, {
            "id" : 12,
            "open" : false,
            "name" : "电建",
            "children" : [ {
                "id" : 13,
                "open" : false,
                "name" : "水电建设",
                "pid" : 12
            }, {
                "id" : 14,
                "open" : false,
                "name" : "火电建设",
                "pid" : 12
            }, {
                "id" : 15,
                "open" : false,
                "name" : "风电建设",
                "pid" : 12
            }, {
                "id" : 16,
                "open" : false,
                "name" : "其他电建",
                "pid" : 12
            } ],
            "pid" : 2
        }, {
            "id" : 17,
            "open" : false,
            "name" : "供电",
            "pid" : 2
        } ],
        "pid" : 1
    }, {
        "id" : 3,
        "open" : false,
        "name" : "建筑行业",
        "children" : [ {
            "id" : 18,
            "open" : false,
            "name" : "房建",
            "pid" : 3
        }, {
            "id" : 19,
            "open" : false,
            "name" : "市政",
            "pid" : 3
        }, {
            "id" : 20,
            "open" : false,
            "name" : "桥梁",
            "pid" : 3
        }, {
            "id" : 21,
            "open" : false,
            "name" : "城市轨道",
            "pid" : 3
        }, {
            "id" : 22,
            "open" : false,
            "name" : "公路",
            "pid" : 3
        }, {
            "id" : 23,
            "open" : false,
            "name" : "水运",
            "pid" : 3
        }, {
            "id" : 24,
            "open" : false,
            "name" : "铁路",
            "pid" : 3
        }, {
            "id" : 25,
            "open" : false,
            "name" : "隧道",
            "pid" : 3
        }, {
            "id" : 26,
            "open" : false,
            "name" : "其他建筑行业",
            "pid" : 3
        } ],
        "pid" : 1
    }, {
        "id" : 4,
        "open" : false,
        "name" : "水利行业",
        "children" : [ {
            "id" : 27,
            "open" : false,
            "name" : "监督",
            "pid" : 4
        }, {
            "id" : 28,
            "open" : false,
            "name" : "运行",
            "pid" : 4
        }, {
            "id" : 29,
            "open" : false,
            "name" : "建设",
            "pid" : 4
        }, {
            "id" : 30,
            "open" : false,
            "name" : "农电",
            "pid" : 4
        }, {
            "id" : 31,
            "open" : false,
            "name" : "水文",
            "pid" : 4
        }, {
            "id" : 32,
            "open" : false,
            "name" : "设计",
            "pid" : 4
        }, {
            "id" : 33,
            "open" : false,
            "name" : "其他水利行业",
            "pid" : 4
        } ],
        "pid" : 1
    }, {
        "id" : 34,
        "open" : false,
        "name" : "冶金行业",
        "pid" : 1
    }, {
        "id" : 35,
        "open" : false,
        "name" : "煤矿行业",
        "pid" : 1
    }, {
        "id" : 36,
        "open" : false,
        "name" : "非煤矿行业",
        "pid" : 1
    }, {
        "id" : 37,
        "open" : false,
        "name" : "工商贸行业",
        "pid" : 1
    }, {
        "id" : 38,
        "open" : false,
        "name" : "危化品行业",
        "pid" : 1
    }, {
        "id" : 39,
        "open" : false,
        "name" : "民爆行业",
        "pid" : 1
    }, {
        "id" : 40,
        "open" : false,
        "name" : "烟花爆竹行业",
        "pid" : 1
    }, {
        "id" : 41,
        "open" : false,
        "name" : "交通行业",
        "pid" : 1
    }, {
        "id" : 42,
        "open" : false,
        "name" : "化工行业",
        "pid" : 1
    }, {
        "id" : 43,
        "open" : false,
        "name" : "机械行业",
        "pid" : 1
    }, {
        "id" : 44,
        "open" : false,
        "name" : "教育行业",
        "pid" : 1
    }, {
        "id" : 45,
        "open" : false,
        "name" : "其他行业",
        "pid" : 1
    } ],
    "pid" : 0
} ];

var business_json = [{"id":"1","pId":"0","text":"全部行业"},{"id":"2","pId":"1","text":"电力行业"},{"id":"3","pId":"1","text":"建筑行业"},{"id":"4","pId":"1","text":"水利行业"},{"id":"5","pId":"2","text":"发电"},{"id":"6","pId":"5","text":"火力发电"},{"id":"7","pId":"5","text":"水力发电"},{"id":"8","pId":"5","text":"风力发电"},{"id":"9","pId":"5","text":"光伏发电"},{"id":"10","pId":"5","text":"燃气发电"},{"id":"11","pId":"5","text":"其他发电"},{"id":"12","pId":"2","text":"电建"},{"id":"13","pId":"12","text":"水电建设"},{"id":"14","pId":"12","text":"火电建设"},{"id":"15","pId":"12","text":"风电建设"},{"id":"16","pId":"12","text":"其他电建"},{"id":"17","pId":"2","text":"供电"},{"id":"18","pId":"3","text":"房建"},{"id":"19","pId":"3","text":"市政"},{"id":"20","pId":"3","text":"桥梁"},{"id":"21","pId":"3","text":"城市轨道"},{"id":"22","pId":"3","text":"公路"},{"id":"23","pId":"3","text":"水运"},{"id":"24","pId":"3","text":"铁路"},{"id":"25","pId":"3","text":"隧道"},{"id":"26","pId":"3","text":"其他建筑行业"},{"id":"27","pId":"4","text":"监督"},{"id":"28","pId":"4","text":"运行"},{"id":"29","pId":"4","text":"建设"},{"id":"30","pId":"4","text":"农电"},{"id":"31","pId":"4","text":"水文"},{"id":"32","pId":"4","text":"设计"},{"id":"33","pId":"4","text":"其他水利行业"},{"id":"34","pId":"1","text":"冶金行业"},{"id":"35","pId":"1","text":"煤矿行业"},{"id":"36","pId":"1","text":"非煤矿行业"},{"id":"37","pId":"1","text":"工商贸行业"},{"id":"38","pId":"1","text":"危化品行业"},{"id":"39","pId":"1","text":"民爆行业"},{"id":"40","pId":"1","text":"烟花爆竹行业"},{"id":"41","pId":"1","text":"交通行业"},{"id":"42","pId":"1","text":"化工行业"},{"id":"43","pId":"1","text":"机械行业"},{"id":"44","pId":"1","text":"教育行业"},{"id":"45","pId":"1","text":"其他行业"}];