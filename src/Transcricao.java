import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Transcricao {

    // Define o caminho do seu projeto NetBeans (MUDE ISSO!)
    // Exemplo: "C:\\Users\\SeuUsuario\\Documents\\NetBeansProjects\\Finance"
    private static final String PROJECT_ROOT = "C:\\Users\\Skidrow-NT\\Documents\\NetBeansProjects\\Finance";
    
    // Define os nomes dos packages a serem INCLUÍDOS na transcrição
    // APENAS arquivos que contiverem um destes nomes no caminho (relativo a src) serão incluídos.
    private static final String INCLUDED_PACKAGE_1 = "Classes"; 
    private static final String INCLUDED_PACKAGE_2 = "Forms"; 
    
    // Define o nome do arquivo de saída
    private static final String OUTPUT_FILE = "ProjectCodeSummary.txt";

    /**
     * Remove comentários e realiza minificação agressiva do código, removendo 
     * espaços e quebras de linha desnecessários, mas mantendo a estrutura 
     * básica (novas linhas após {, } e ;).
     * * @param content O conteúdo bruto do arquivo Java.
     * @return O conteúdo do código limpo e minificado.
     */
    private static String cleanJavaCode(String content) {
        // 1. Remove comentários em bloco (/* ... */ e /** ... */)
        // O padrão [\s\S]*? corresponde a qualquer caractere (incluindo quebras de linha) de forma não-gananciosa.
        String noBlockComments = content.replaceAll("/\\*([\\s\\S]*?)\\*/", " ");
        
        // 2. Remove comentários em linha (//...)
        String noComments = noBlockComments.replaceAll("//.*", " ");

        // 3. Normaliza todo o whitespace (espaços, tabs, novas linhas) para um único espaço
        String normalizedWhitespace = noComments.replaceAll("\\s+", " ");

        // 4. Remove espaços ao redor de pontuações e operadores
        // Remove espaço antes/depois de: , ; ) ] } :
        String compressed = normalizedWhitespace.replaceAll("\\s*([,;)\\]\\}:])\\s*", "$1");
        // Remove espaço depois de: ( [ { .
        compressed = compressed.replaceAll("([(\\[{.])\\s*", "$1");
        // Remove espaço antes/depois de operadores (+, -, *, /, =, <, >, !, &, |, %)
        compressed = compressed.replaceAll("\\s*([+\\-*/=<>!&|%])\\s*", "$1");
        // Coloca um espaço de volta ao redor de operadores binários para evitar juntar palavras-chave (ex: publicstatic)
        compressed = compressed.replaceAll("([a-zA-Z0-9])([+\\-*/=<>!&|%])([a-zA-Z0-9])", "$1 $2 $3");
        
        // 5. Adiciona quebras de linha estratégicas para manter a legibilidade por blocos
        // Adiciona nova linha após ;
        compressed = compressed.replaceAll(";", ";\n");
        // Adiciona nova linha após {
        compressed = compressed.replaceAll("\\{", "{\n");
        // Adiciona nova linha antes/depois de } (exceto se for o início da string)
        compressed = compressed.replaceAll("(?<!^)\\}", "\n}\n"); 

        // 6. Limpa múltiplas novas linhas resultantes e trim
        compressed = compressed.replaceAll("(\n){2,}", "\n").trim();
        
        return compressed;
    }

    private static void generateCodeSummary() {
        Path projectPath = Paths.get(PROJECT_ROOT);
        // Assume a estrutura padrão do NetBeans (src/)
        Path srcPath = projectPath.resolve("src"); 
        Path outputPath = projectPath.resolve(OUTPUT_FILE);

        if (!Files.exists(srcPath)) {
            System.err.println("Erro: Não foi possível encontrar a pasta 'src' em: " + projectPath);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()));
             Stream<Path> paths = Files.walk(srcPath)) {

            writer.write("=================================================================\n");
            writer.write("           Sumário de Código do Projeto - Packages Inclusos: " + INCLUDED_PACKAGE_1 + ", " + INCLUDED_PACKAGE_2 + "\n");
            writer.write("=================================================================\n\n");

            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".java"))
                 .forEach(filePath -> {
                     try {
                         // 1. Obtém o nome do arquivo e o caminho relativo
                         String fileName = filePath.getFileName().toString();
                         Path relativePath = srcPath.relativize(filePath);
                         
                         // 2. Verifica se o caminho pertence a um dos packages incluídos
                         String pathString = relativePath.toString();
                         boolean isIncluded = pathString.contains(INCLUDED_PACKAGE_1) || pathString.contains(INCLUDED_PACKAGE_2);

                         if (!isIncluded) {
                             System.out.println("Pulando arquivo (Package não incluído): " + fileName);
                             return; // Pula este arquivo se não for 'Classes' nem 'Forms'
                         }

                         // 3. Lê e LIMPA o conteúdo do arquivo
                         String rawContent = new String(Files.readAllBytes(filePath));
                         String content = cleanJavaCode(rawContent);

                         // Se o arquivo só tinha comentários, não precisamos incluí-lo
                         if (content.isEmpty()) {
                             System.out.println("Pulando arquivo (Apenas comentários): " + fileName);
                             return;
                         }

                         // 4. Escreve no arquivo de saída
                         writer.write("############################################################\n");
                         writer.write("# Arquivo Java: " + relativePath.toString().replace('\\', '/') + "\n");
                         writer.write("############################################################\n\n");
                         writer.write(content);
                         writer.write("\n\n");
                         
                         System.out.println("Processado (Código Minificado): " + fileName);

                     } catch (IOException e) {
                         System.err.println("Erro ao processar o arquivo: " + filePath + " - " + e.getMessage());
                     }
                 });

            System.out.println("\n--- Geração Concluída! ---");
            System.out.println("Arquivo gerado em: " + outputPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro ao acessar o sistema de arquivos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Para rodar, apenas chame o método estático:
        generateCodeSummary();
    }
}
