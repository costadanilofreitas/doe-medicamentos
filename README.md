#### **API REST Sistema Doe Medicamentos**

No arquivo "DoeMedicamentos.postman_collection.json" é um arquivo do Postman para testes em localhost, e "DoeMedicamentosAWS.postman_collection.json". 
para testes na máquina AWS informada abaixo.
Nele contém uma Collection com todas as chamadas dpara o sistema.

Este sistema usa o banco de dados MariaDB e para a criação do bancode dados deve usar o script abaixo:

    Criar usuário:
        CREATE USER 'doe_med'@'localhost' IDENTIFIED BY 'doe123';
    Criar banco de dados:
        CREATE DATABASE doe_medicamentos;
    Acesso ao usuário:
        GRANT ALL ON doe_medicamentos.* TO 'doe_med'@'%' IDENTIFIED BY 'doe123' WITH GRANT OPTION;


Maquina AWS criada para projeto: 18.230.155.76

`~~Classe~~`

 
**_Usuário_**:

Atributos:

    **id**: identificação do usuário
    **nome**: Nome do usuário
    **email**: email do usuário
    **senha**: senha do usuário

URL: http://18.230.155.76:8080/usuario

Verbos:
    
**POST**

    - Incluir usuário
        URL: http://18.230.155.76:8080/usuario/registrar
    Body:
            {
            	"nome":"nome do usuario",
            	"email": "email@gmail.com",
            	"senha": "senha"
            }	
    

 
**_Medicamento_**:

Somente usuários cadastrados no sistema poderão realizar a manutenção de medicamentos 

Atributos:

    **id**: identificação do medicamento
    **nome**: Nome do medicamento
    **laboratório**: nome do laboratório
    **controlado**: Informa se é medicamento controlado

URL: http://18.230.155.76:8080/medicamentos

Verbos:
 
**GET**:

    - Buscar todos medicamentos
        URL: http://18.230.155.76:8080/medicamentos
    - Buscar medicamentos por id 
        URL: http://18.230.155.76:8080/medicamentos/<id>
    
**POST**

    - Incluir medicamentos
        URL: http://18.230.155.76:8080/medicamentos
    Body:
            {
                "nome":"Nome medicamento",
                "laboratorio":"Nome Laboratório",
                "controlado": true/false
            }
    
**PUT**

    - Alterar medicamentos
        URL: http://18.230.155.76:8080/medicamentos/<id>
    Body:
            {
                "nome":"Nome medicamento",
                "laboratorio":"Nome Laboratório",
                "controlado": true/false
            }
    
 
  **DELETE**
  
    - Excluir medicamentos
        URL: http://18.230.155.76:8080/medicamentos/<id>
    
    
  
    
  **_Paciente_**:
    
    Atributos:
    
    **idPaciente**: identificação do paciente
    **nome**: Nome do paciente
    **email**: e-mail do paciente
    **telefone**: Informa se é medicamento controlado

URL: http://18.230.155.76:8080/paciente

Verbos:
 
**GET**:

      - Buscar paciente por id 
         URL: http://18.230.155.76:8080/paciente
    
**POST**

    - Incluir paciente
         URL: http://18.230.155.76:8080/paciente
    Body:
            {
                "nome": "Nome do paciente",
                "telefone": "telefone do paciente",
                "email": "emailpaciente@gmail.com",
                "dataNascimento": "AAAA-MM-DD",
                "endereco" : {
                                "endereco" : "Endereco ",
                                "numero" : "numero",
                                "complemento" : "complemento do endereco",
                                "cidade" : "cidade",
                                "estado" : "estado"
                             }
            }
    
**PUT**

    - Alterar paciente
        URL: http://18.230.155.76:8080/paciente/<id>
    Body:
            {
                "nome": "Nome do paciente",
                "telefone": "telefone do paciente",
                "email": "emailpaciente@gmail.com",
                "dataNascimento": "AAAA-MM-DD",
                "endereco" : {
                                "idEndereco": "Opcional caso não seja endereço novo",
                                "endereco" : "Endereco ",
                                "numero" : "numero",
                                "complemento" : "complemento do endereco",
                                "cidade" : "cidade",
                                "estado" : "estado"
                             }
            }
    
 
  **DELETE**
  
    - Excluir paciente
        URL: http://18.230.155.76:8080/paciente/<id>
    
    
 
   **_Endereço_**:
     
     Atributos:
     
     **idEndereco**: identificação do endereco
     **endereco**: Endereço do paciente
     **complemento**: complemento do endereço do paciente
     **cidade**: Cidade do paciente
     **Estado**: Estado do paciente
               
 
 URL: http://18.230.155.76:8080/endereco
 
 Verbos:
  
 **GET**:
 
       - Buscar endereço por id
            URL: http://18.230.155.76:8080/endereco/<id>
       - Buscar todos os endereços 
            URL: http://18.230.155.76:8080/endereco/todosenderecos
     
 **POST**
        
     - Incluir endereço 
     URL: http://18.230.155.76:8080/endereco/incluir
     Body:
            {
                "endereco" : "Endereco ",
                "numero" : "numero",
                "complemento" : "complemento do endereco",
                "cidade" : "cidade",
                "estado" : "estado"
            }
    
     
 **PUT**
 
        - Alterar paciente
            URL: http://18.230.155.76:8080/endereco/<id>
        Body:
            {
                "endereco" : "Endereco ",
                "numero" : "numero",
                "complemento" : "complemento do endereco",
                "cidade" : "cidade",
                "estado" : "estado"
            }
        - Buscar endereço por Estado
            URL: http://18.230.155.76:8080/endereco/estado
            Body:
                    {
            	        "estado": "estado de pesquisa"
                    }
        - Buscar endereço por Cidade e Estado
            URL: http://18.230.155.76:8080/endereco/estadocidade
            Body:
                    {
            	        "estado": "estado de pesquisa"
            	        "cidade": "cidade de pesquisa"
                    }
        - Buscar endereço por Cidade
            URL: http://18.230.155.76:8080/endereco/cidade
            Body:
                    {
            	        "cidade": "cidade de pesquisa"
                    }
     
  
   **DELETE**
   
     - Excluir paciente
      URL: http://18.230.155.76:8080/endereco/<id>
           
 **_Doação_**:
  
       Atributos:
       
       **idDoacao**: identificação da doação
       **dataValidade**:Data de validade do remédio a ser doado
       **dataCadastro**: data de cadastro da doação
       **medicamento**: Medicamento que será doado
       **paciente**: Paciaente que está doando o medicamento
                 
   
   URL: http://18.230.155.76:8080/doacao
   
   Verbos:
    
   **GET**:
   
         - Buscar endereço por id
              URL: http://18.230.155.76:8080/doacao/<id>
         - Buscar todos as doações 
              URL: http://18.230.155.76:8080/doacao
         - Buscar doação por medicamento
              URL: 18.230.155.76:8080/doacao/buscarDoacaoPorMedicamento/<id>
         
   **POST**
          
       - Incluir doação 
       URL: http://18.230.155.76:8080/doacao
       Body:
                {
                    "dataValidade" : "AAAA-MM-DD",
                    "dataCadastro" : "AAAA-MM-DD",
                    "medicamento" : "{id}",
                    "paciente" : "{idPaciente}"
                }
      
       
   **PUT**
   
       - Alterar doação
       URL: http://18.230.155.76:8080/doacao/<id>
       Body:
              {
                   "dataValidade" : "AAAA-MM-DD",
                   "dataCadastro" : "AAAA-MM-DD",
                   "medicamento" : "{id}",
                   "paciente" : "{idPaciente}"
              }
       
    
   **DELETE**
     
       - Excluir doação
        URL: http://18.230.155.76:8080/doacao/<id>
    

**_Reserva_**:
  
       Atributos:
       
       **idReserva**: identificação da reserva
       **CRM**:CRM do médico 
       **dataReceita**: data de receida do remedio solicitado
       **status**: Status da doação (RESERVADO, FINALIZADA)
       **data_baixa**: Data que foi realizada a entrega do medicamento doado
       **doacao**: Qual a doação que está sendo solicitada
       **paciente**: Paciente que está realizando a reserva da doação
                 
   
   URL: http://18.230.155.76:8080/reserva
   
   Verbos:
    
   **GET**:
   
         - Buscar reserva por id
              URL: http://18.230.155.76:8080/reserva/<id>

         
   **POST**
          
       - Incluir reserva 
       URL: http://18.230.155.76:8080/reserva
       Body:
               {
               	"status":"RESERVADO",
               	"doacao":{
                   	        "idDocacao": Identificador da doação que está sendo reservada                         },
                 "paciente":{
                   	            "idPaciente": Identificador do paciente que está realizando a reserva
                            }
               	"CRM": "somente para remedios controlados"
               	"dataReceita": "somente para remedios controlados"
               }
      
       
   **PUT**
   
       - Alterar reserva
       URL: http://18.230.155.76:8080/reserva/<id>
       Body:
               {
               	"status":"RESERVADO",
               	"doacao":{
                   	        "idDocacao": Identificador da doação que está sendo reservada                         },
                 "paciente":{
                   	            "idPaciente": Identificador do paciente que está realizando a reserva
                            }
               	"CRM": "somente para remedios controlados"
               	"dataReceita": "somente para remedios controlados"
               }
               
       - Finalizar reserva
       URL: http://18.230.155.76:8080/reserva/finalizar<id>
       Body:
       data_baixa
                      {
                      	"status":"FINALIZADA",
                      	"doacao":{
                          	        "idDocacao": Identificador da doação que está sendo reservada                         },
                        "paciente":{
                          	            "idPaciente": Identificador do paciente que está realizando a reserva
                                   }
                      	"CRM": "somente para remedios controlados"
                      	"dataReceita": "somente para remedios controlados"
                      	"data_baixa": Data que foi entregue o remédio, caso não seja preenchida será a data da execução
                      }
       
    
   **DELETE**
     
       - Excluir reserva
        URL: http://18.230.155.76:8080/reserva/<id>
    
        