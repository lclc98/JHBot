import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class Generate {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withUrl("jdbc:sqlite:jhbot.sqlite")
                )
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.sqlite.SQLiteDatabase")
                                .withExcludes("sqlite_master|flyway_schema_history")
                        ).withTarget(new Target()
                                .withPackageName("com.plumealerts.jhbot.db")
                                .withDirectory("src/main/java")
                        )
                );
        GenerationTool.generate(configuration);
    }
}
