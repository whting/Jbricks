package mybatis.top;

import cn.jbricks.toolkit.clazz.ClassUtil;
import cn.jbricks.toolkit.top.gen.JsRPCGenerator;

/**
 * Created by kuiyuexiang on 15/12/3.
 */
public class TopGenerator {

    private static final String srcPackage = "cn.jbricks";

    private static final String javaTargetPackage = "cn.bihu.app.top";
    private static final String javaTargetPath = "/Users/kuiyuexiang/Code/jbricks/generator/src/target/java";

    private static final String ocTargetPackage = "Top";
    private static final String ocTargetPath = "/Users/kuiyuexiang/Code/jbricks/generator/src/target/objectc";

    private static final String jsTargetPackage = "top";
    private static final String jsTargetPath = "F:/bihu/generator/src/target/javascript";
    /*private static final String jsTargetPath = "/Users/kuiyuexiang/Code/jbricks/generator/src/target/javascript";    private static final String jsTargetPath = "/Users/kuiyuexiang/Code/jbricks/generator/src/target/javascript";*/


    public static final void main(String[] arg) {
   //     new AndroidRPCGenerator().execute(ClassUtil.getClasssFromPackage(srcPackage), javaTargetPath, javaTargetPackage);
   //     new OCRPCGenerator("Constant", "Service", "Model", "Top").execute(ClassUtil.getClasssFromPackage(srcPackage), ocTargetPath, ocTargetPackage);
        new JsRPCGenerator("constant", "service", "model", "top").execute(ClassUtil.getClasssFromPackage(srcPackage), jsTargetPath, jsTargetPackage);

    }
}

