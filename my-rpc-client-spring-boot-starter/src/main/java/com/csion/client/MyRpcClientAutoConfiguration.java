package com.csion.client;

import com.csion.client.annotation.MyRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * Created by csion on 2022/2/18 16:43.
 *
 *      1，扫描出所有的接口存到cache中
 *      2，@bean（properties）
 *      3，@bean（nettyClient），并在init方法中初始化
 *
 */
@Slf4j
public class MyRpcClientAutoConfiguration implements ImportBeanDefinitionRegistrar {

    /**
     * 1 扫描代理接口
     * @param annotationMetadata
     * @param beanDefinitionRegistry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            // 获取spring启动类的包路径，后面扫描该包下的所有的接口
            String appClassName = annotationMetadata.getClassName();
            Class<?> appClass = Class.forName(appClassName);
            String packageName = appClass.getPackage().getName();

            // 实例化ClassPathScanningCandidateComponentProvider用于扫描
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
                Map<String, Object> annotationAttributes = metadataReader.getAnnotationMetadata().getAnnotationAttributes(MyRpcService.class.getName());
                if (annotationAttributes != null) {
                    ScanInterfacesCache.addInterface(metadataReader.getClassMetadata().getClassName());
                    return true;
                }
                return false;
            });
            scanner.findCandidateComponents(packageName);

        } catch (Exception e) {
            log.error("rpc扫描代理类异常", e);
        }
    }
}
