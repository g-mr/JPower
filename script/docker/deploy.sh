#使用说明，用来提示输入参数
usage() {
  echo "Usage: sh 执行脚本.sh [base|core|stop|rm|rmiNoneTag]"
  exit 1
}

#启动基础支撑模块
base(){
  docker-compose up -d nacos sentinel seata elasticsearch skywalking-oap skywalking-ui
}

#启动核心程序模块
core(){
  docker-compose up -d jpower-gateway jpower-auth jpower-user jpower-system jpower-file
}

#关闭核心程序模块
stopCore(){
  docker-compose stop jpower-gateway jpower-auth jpower-user jpower-system jpower-file
}

#关闭所有模块
stop(){
  docker-compose stop
}

#删除所有模块
rm(){
  docker-compose rm
}

#删除Tag为空的镜像
rmiNoneTag(){
  docker images|grep none|awk '{print $3}'|xargs docker rmi -f
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"base")
  base
;;
"core")
  core
;;
"stop")
  stop
;;
"rm")
	rm
;;
"rmiNoneTag")
  rmiNoneTag
;;
"stopCore")
  stopCore
;;
*)
  usage
;;
esac