package com.pig.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MPGenerator {

    public static void main(String[] args) {
        //创建代码生成器
        AutoGenerator mpg = new AutoGenerator();
        //指定模板引擎  默认velocity
        //mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        //全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOpen(false);
        gc.setOutputDir("D:\\usr\\local\\tst\\");
        gc.setFileOverride(true); //是否覆盖已有文件
        gc.setBaseResultMap(true); //XML是否需要BaseResultMap
        gc.setBaseColumnList(true); //XML是否显示字段
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setAuthor("");
        gc.setIdType(IdType.AUTO);
        mpg.setGlobalConfig(gc);
        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://10.100.6.76:3306/rd_ess1.0.3?characterEncoding=utf8&useSSL=false");
        dsc.setUsername("root");
        dsc.setPassword("berrybio");
        mpg.setDataSource(dsc);
        //策略配置
        StrategyConfig sc = new StrategyConfig();
        sc.setInclude("rd_ess_sampleinfo");
//        sc.setTablePrefix("syn_"); //表名前缀
        sc.setNaming(NamingStrategy.underline_to_camel); //表名生成策略
        sc.setEntityBuilderModel(true);
        sc.setEntityLombokModel(true);
//        sc.setSuperEntityClass("com.berryplatform.model.BaseModel");
//        // 公共父类
//        sc.setSuperControllerClass("com.berryplatform.model.BaseModel");
//        // 写于父类中的公共字段
//        sc.setSuperEntityColumns("ENABLE","ADDTIME","CREATOR","CREATE_USERNAME","LASTUPDATETIME","LASTUPDATOR","UPDATE_USERNAME");
        mpg.setStrategy(sc);
        //包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.pig.modules.wes");
        pc.setEntity("entity");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("serviceImpl");
        pc.setMapper("mapper");
        pc.setXml("mapperXml");
        mpg.setPackageInfo(pc);

        mpg.execute();
    }
}
