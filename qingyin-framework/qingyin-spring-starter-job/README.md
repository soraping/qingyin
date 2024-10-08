### xxl-job 分布式任务调度平台

仓库地址

https://github.com/xuxueli/xxl-job

https://gitee.com/xuxueli0323/xxl-job

#### 初始化数据库

`tables_xxl_job.sql` 共8张表：

> - xxl_job_lock：任务调度锁表；
> - xxl_job_group：执行器信息表，维护任务执行器信息；
> - xxl_job_info：调度扩展信息表： 用于保存XXL-JOB调度任务的扩展信息，如任务分组、任务名、机器地址、执行器、执行入参和报警邮件等等；
> - xxl_job_log：调度日志表： 用于保存XXL-JOB任务调度的历史信息，如调度结果、执行结果、调度入参、调度机器和执行器等等；
> - xxl_job_logglue：任务GLUE日志：用于保存GLUE更新历史，用于支持GLUE的版本回溯功能；
> - xxl_job_registry：执行器注册表，维护在线的执行器和调度中心机器地址信息；
> - xxl_job_user：系统用户表；

#### 部署

```bash
docker pull xuxueli/xxl-job-admin:2.4.1
```

```yaml
version: "3"
services:
  xxljob:
    image: xuxueli/xxl-job-admin:2.4.1
    container_name: xxljob
    privileged: true
    ports:
      - 9003:8080
    volumes:
      - /www/wwwroot/data/xxl-job/logs:/data/applogs
    environment:
      PARAMS: "--spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_job?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false
                   --spring.datasource.username=root
                   --spring.datasource.password=123456
                   --server.servlet.context-path=/xxl-job-admin
                   --spring.mail.host=smtp.qq.com
                   --spring.mail.port=25
                   --spring.mail.username=xxx@qq.com
                   --spring.mail.from=xxx@qq.com
                   --spring.mail.password=xxx
                   --xxl.job.accessToken="
```

#### 访问

浏览器访问 `http://your_server_ip:8080/xxl-job-admin` ，账号密码 `admin/123456`