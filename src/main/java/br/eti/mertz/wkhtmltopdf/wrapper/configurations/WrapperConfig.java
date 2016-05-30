package br.eti.mertz.wkhtmltopdf.wrapper.configurations;

public class WrapperConfig {
    private String wkhtmltopdfCommand = "wkhtmltopdf";
    private String wkhtmltoimageCommand = "wkhtmltoimage";

    public WrapperConfig(String wkhtmltopdfCommand) {
        this.wkhtmltopdfCommand = wkhtmltopdfCommand;
    }

    public String getWkhtmltopdfCommand() {
        return wkhtmltopdfCommand;
    }

    public String getWkhtmltoimageCommand() {
        return wkhtmltoimageCommand;
    }

}
