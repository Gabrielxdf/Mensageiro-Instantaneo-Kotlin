package com.example.mensageiroinstantaneo

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuthException

open class Utils : AppCompatActivity() {

    //Facilitar toast
    fun toast(mensagem: String?, length: Int = Toast.LENGTH_LONG) {
        Toast.makeText(this, mensagem, length).show()
    }

    //Traduzir os erros do Firebase para português
    fun traduz_erro(errorCode: FirebaseAuthException): String? {

        val retorno = when (errorCode.errorCode) {
            "ERROR_APP_DELETED" ->
                "O banco de dados não foi localizado."
            "ERROR_EXPIRED_ACTION_CODE" ->
                "O código da ação o ou link expirou."
            "ERROR_INVALID_ACTION_CODE" ->
                "O código da ação é inválido. Isso pode acontecer se o código estiver malformado ou já tiver sido usado."
            "ERROR_USER_DISABLED" ->
                "O usuário correspondente à credencial fornecida foi desativado."
            "ERROR_USER_NOT_FOUND" ->
                "O usuário não correponde à nenhuma credencial."
            "ERROR_WEAK_PASSWORD" ->
                "A senha é muito fraca."
            "ERROR_EMAIL_ALREADY_IN_USE" ->
                "Já existe uma conta com o endereço de email fornecido."
            "ERROR_INVALID_EMAIL" ->
                "O endereço de e-mail não é válido."
            "ERROR_OPERATION_NOT_ALLOWED" ->
                "O tipo de conta correspondente à esta credencial, ainda não encontra_se ativada."
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" ->
                "E_mail já associado a outra conta."
            "ERROR_AUTH_DOMAIN_CONFIG_REQUIRED" ->
                "A configuração para autenticação não foi fornecida."
            "ERROR_CREDENTIAL_ALREADY_IN_USE" ->
                "Já existe uma conta para esta credencial."
            "ERROR_OPERATION_NOT_SUPPORTED_IN_THIS_ENVIRONMENT" ->
                "Esta operação não é suportada no ambiente que está sendo executada. Verifique se deve ser http ou https."
            "ERROR_TIMEOUT" ->
                "Excedido o tempo de resposta. O domínio pode não estar autorizado para realizar operações."
            "ERROR_MISSING_ANDROID_PKG_NAME" ->
                "Deve ser fornecido um nome de pacote para instalação do aplicativo Android."
            "ERROR_MISSING_CONTINUE_URI" ->
                "A próxima URL deve ser fornecida na solicitação."
            "ERROR_MISSING_IOS_BUNDLE_ID" ->
                "Deve ser fornecido um nome de pacote para instalação do aplicativo iOS."
            "ERROR_INVALID_CONTINUE_URI" ->
                "A próxima URL fornecida na solicitação é inválida."
            "ERROR_UNAUTHORIZED_CONTINUE_URI" ->
                "O domínio da próxima URL não está na lista de autorizações."
            "ERROR_INVALID_DYNAMIC_LINK_DOMAIN" ->
                "O domínio de link dinâmico fornecido, não está autorizado ou configurado no projeto atual."
            "ERROR_ARGUMENT_ERROR" ->
                "Verifique a configuração de link para o aplicativo."
            "ERROR_INVALID_PERSISTENCE_TYPE" ->
                "O tipo especificado para a persistência dos dados é inválido."
            "ERROR_UNSUPPORTED_PERSISTENCE_TYPE" ->
                "O ambiente atual não suportar o tipo especificado para persistência dos dados."
            "ERROR_INVALID_CREDENTIAL" ->
                "A credencial expirou ou está mal formada."
            "ERROR_WRONG_PASSWORD" ->
                "Senha incorreta."
            "ERROR_INVALID_VERIFICATION_CODE" ->
                "O código de verificação da credencial não é válido."
            "ERROR_INVALID_VERIFICATION_ID" ->
                "O ID de verificação da credencial não é válido."
            "ERROR_CUSTOM_TOKEN_MISMATCH" ->
                "O token está diferente do padrão solicitado."
            "ERROR_INVALID_CUSTOM_TOKEN" ->
                "O token fornecido não é válido."
            "ERROR_CAPTCHA_CHECK_FAILED" ->
                "O token de resposta do reCAPTCHA não é válido, expirou ou o domínio não é permitido."
            "ERROR_INVALID_PHONE_NUMBER" ->
                "O número de telefone está em um formato inválido (padrão E.164)."
            "ERROR_MISSING_PHONE_NUMBER" ->
                "O número de telefone é requerido."
            "ERROR_QUOTA_EXCEEDED" ->
                "A cota de SMS foi excedida."
            "ERROR_CANCELLED_POPUP_REQUEST" ->
                "Somente uma solicitação de janela pop_up é permitida de uma só vez."
            "ERROR_POPUP_BLOCKED" ->
                "A janela pop_up foi bloqueado pelo navegador."
            "ERROR_POPUP_CLOSED_BY_USER" ->
                "A janela pop_up foi fechada pelo usuário sem concluir o login no provedor."
            "ERROR_UNAUTHORIZED_DOMAIN" ->
                "O domínio do aplicativo não está autorizado para realizar operações."
            "ERROR_INVALID_USER_TOKEN" ->
                "O usuário atual não foi identificado."
            "ERROR_USER_TOKEN_EXPIRED" ->
                "O token do usuário atual expirou."
            "ERROR_NULL_USER" ->
                "O usuário atual é nulo."
            "ERROR_APP_NOT_AUTHORIZED" ->
                "Aplicação não autorizada para autenticar com a chave informada."
            "ERROR_INVALID_API_KEY" ->
                "A chave da API fornecida é inválida."
            "ERROR_NETWORK_REQUEST_FAILED" ->
                "Falha de conexão com a rede."
            "ERROR_REQUIRES_RECENT_LOGIN" ->
                "O último horário de acesso do usuário não atende ao limite de segurança."
            "ERROR_TOO_MANY_REQUESTS" ->
                "As solicitações foram bloqueadas devido a atividades incomuns. Tente novamente depois que algum tempo."
            "ERROR_WEB_STORAGE_UNSUPPORTED" ->
                "O navegador não suporta armazenamento ou se o usuário desativou este recurso."
            "ERROR_INVALID_CLAIMS" ->
                "Os atributos de cadastro personalizado são inválidos."
            "ERROR_CLAIMS_TOO_LARGE" ->
                "O tamanho da requisição excede o tamanho máximo permitido de 1 Megabyte."
            "ERROR_ID_TOKEN_EXPIRED" ->
                "O token informado expirou."
            "ERROR_ID_TOKEN_REVOKED" ->
                "O token informado perdeu a validade."
            "ERROR_INVALID_ARGUMENT" ->
                "Um argumento inválido foi fornecido a um método."
            "ERROR_INVALID_CREATION_TIME" ->
                "O horário da criação precisa ser uma data UTC válida."
            "ERROR_INVALID_DISABLED_FIELD" ->
                "A propriedade para usuário desabilitado é inválida."
            "ERROR_INVALID_DISPLAY_NAME" ->
                "O nome do usuário é inválido."
            "ERROR_INVALID_EMAIL_VERIFIED" ->
                "O e_mail é inválido."
            "ERROR_INVALID_HASH_ALGORITHM" ->
                "O algoritmo de HASH não é uma criptografia compatível."
            "ERROR_INVALID_HASH_BLOCK_SIZE" ->
                "O tamanho do bloco de HASH não é válido."
            "ERROR_INVALID_HASH_DERIVED_KEY_LENGTH" ->
                "O tamanho da chave derivada do HASH não é válido."
            "ERROR_INVALID_HASH_KEY" ->
                "A chave de HASH precisa ter um buffer de byte válido."
            "ERROR_INVALID_HASH_MEMORY_COST" ->
                "O custo da memória HASH não é válido."
            "ERROR_INVALID_HASH_PARALLELIZATION" ->
                "O carregamento em paralelo do HASH não é válido."
            "ERROR_INVALID_HASH_ROUNDS" ->
                "O arredondamento de HASH não é válido."
            "ERROR_INVALID_HASH_SALT_SEPARATOR" ->
                "O campo do separador de SALT do algoritmo de geração de HASH precisa ser um buffer de byte válido."
            "ERROR_INVALID_ID_TOKEN" ->
                "O código do token informado não é válido."
            "ERROR_INVALID_LAST_SIGN_IN_TIME" ->
                "O último horário de login precisa ser uma data UTC válida."
            "ERROR_INVALID_PAGE_TOKEN" ->
                "A próxima URL fornecida na solicitação é inválida."
            "ERROR_INVALID_PASSWORD" ->
                "A senha é inválida, precisa ter pelo menos 6 caracteres."
            "ERROR_INVALID_PASSWORD_HASH" ->
                "O HASH da senha não é válida."
            "ERROR_INVALID_PASSWORD_SALT" ->
                "O SALT da senha não é válido."
            "ERROR_INVALID_PHOTO_URL" ->
                "A URL da foto de usuário é inválido."
            "ERROR_INVALID_PROVIDER_ID" ->
                "O identificador de provedor não é compatível."
            "ERROR_INVALID_SESSION_COOKIE_DURATION" ->
                "A duração do COOKIE da sessão precisa ser um número válido em milissegundos, entre 5 minutos e 2 semanas."
            "ERROR_INVALID_UID" ->
                "O identificador fornecido deve ter no máximo 128 caracteres."
            "ERROR_INVALID_USER_IMPORT" ->
                "O registro do usuário a ser importado não é válido."
            "ERROR_INVALID_PROVIDER_DATA" ->
                "O provedor de dados não é válido."
            "ERROR_MAXIMUM_USER_COUNT_EXCEEDED" ->
                "O número máximo permitido de usuários a serem importados foi excedido."
            "ERROR_MISSING_HASH_ALGORITHM" ->
                "É necessário fornecer o algoritmo de geração de HASH e seus parâmetros para importar usuários."
            "ERROR_MISSING_UID" ->
                "Um identificador é necessário para a operação atual."
            "ERROR_RESERVED_CLAIMS" ->
                "Uma ou mais propriedades personalizadas fornecidas usaram palavras reservadas."
            "ERROR_SESSION_COOKIE_REVOKED" ->
                "O COOKIE da sessão perdeu a validade."
            "ERROR_UID_ALREAD_EXISTS" ->
                "O indentificador fornecido já está em uso."
            "ERROR_EMAIL_ALREADY_EXISTS" ->
                "O e_mail fornecido já está em uso."
            "ERROR_PHONE_NUMBER_ALREADY_EXISTS" ->
                "O telefone fornecido já está em uso."
            "ERROR_PROJECT_NOT_FOUND" ->
                "Nenhum projeto foi encontrado."
            "ERROR_INSUFFICIENT_PERMISSION" ->
                "A credencial utilizada não tem permissão para acessar o recurso solicitado."
            "ERROR_INTERNAL_ERROR" ->
                "O servidor de autenticação encontrou um erro inesperado ao tentar processar a solicitação."
            else -> null
        }
        //se o retorno for null retorna o código da mensagem
        return retorno ?: return errorCode.message
    }
}