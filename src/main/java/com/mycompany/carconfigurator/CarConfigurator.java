

package com.mycompany.carconfigurator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class CarConfigurator {

    public static void main(String[] args) {
        // Usamos invokeLater para garantir que a UI seja criada na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }

    // Classe interna para o menu principal
    static class MainMenu extends JFrame {
        public MainMenu() {
            setTitle("Car Configurator - Menu");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(600, 420);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            // Título da aplicação
            JLabel title = new JLabel("Bem-vindo ao Configurador de Carros", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            title.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            add(title, BorderLayout.NORTH);

            // Painel central com GridBagLayout para melhor organização dos componentes
            JPanel center = new JPanel(new GridBagLayout());
            center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6, 6, 6, 6);
            c.fill = GridBagConstraints.HORIZONTAL;

            // Texto informativo
            JLabel info = new JLabel("Crie seu carro ideal: escolha modelos, cores, acessórios e veja o preço em tempo real.");
            info.setHorizontalAlignment(SwingConstants.CENTER);
            c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
            center.add(info, c);

            // Botão para iniciar o configurador
            JButton btnStart = new JButton("Abrir Configurador");
            btnStart.setPreferredSize(new Dimension(220, 36));
            c.gridy = 1; c.gridwidth = 1; c.gridx = 0;
            center.add(btnStart, c);

            JButton btnExit = new JButton("Sair");
            btnExit.setPreferredSize(new Dimension(120, 36));
            c.gridx = 1;
            center.add(btnExit, c);

            JLabel credits = new JLabel("Design: simples e responsivo — feito com Swing");
            credits.setFont(new Font("SansSerif", Font.ITALIC, 12));
            c.gridy = 2; c.gridx = 0; c.gridwidth = 2;
            center.add(credits, c);

            add(center, BorderLayout.CENTER);

            // Barra de progresso decorativa
            JProgressBar progress = new JProgressBar(0, 100);
            progress.setValue(60);
            progress.setStringPainted(true);
            progress.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            add(progress, BorderLayout.SOUTH);

            // Botão de iniciar configurador
            btnStart.addActionListener(e -> {
                Configurator conf = new Configurator(this);
                conf.setVisible(true);
                this.setVisible(false);
            });

            // Botão de sair
            btnExit.addActionListener(e -> System.exit(0));
        }
    }

    // Classe interna para a tela de configuração do carro
    static class Configurator extends JFrame {
        // Componentes da interface
        private JFrame parent;
        private JComboBox<String> modelCombo;
        private JRadioButton enginePetrol, engineHybrid, engineElectric;
        private JCheckBox sunroof, leatherSeats, sportsPack, gps;
        private JSlider hpSlider;
        private JButton colorButton, applyButton, backButton, checkoutButton;
        private JLabel priceLabel, previewLabel, hpValueLabel;
        private JSpinner quantitySpinner;
        private JList<String> wheelList;
        private JTextArea notesArea;
        private JToggleButton sportToggle;
        private JProgressBar calcProgress;

        // Variáveis de estado
        private Color chosenColor = new Color(30, 144, 255);
        private double currentPrice = 0.0;

        public Configurator(JFrame parent) {
            this.parent = parent;
            //configuração básica da janela
            setTitle("Configurar Carro");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(980, 640);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            createMenuBar();
            createConfigurator();
        }

        // Método para criar a barra de menu
        private void createMenuBar() {
            JMenuBar menuBar = new JMenuBar();
            JMenu file = new JMenu("Arquivo");
            JMenuItem save = new JMenuItem("Salvar Configuração");
            JMenuItem load = new JMenuItem("Carregar Configuração");
            JMenuItem exit = new JMenuItem("Sair");
            file.add(save); file.add(load); file.addSeparator(); file.add(exit);

            JMenu help = new JMenu("Ajuda");
            JMenuItem about = new JMenuItem("Sobre");
            help.add(about);

            menuBar.add(file);
            menuBar.add(help);
            setJMenuBar(menuBar);

            // Itens de menu
            exit.addActionListener(e -> System.exit(0));
            about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    "Car Configurator\nVersão de demonstração\nFeito com Swing","Sobre", JOptionPane.INFORMATION_MESSAGE));
            save.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidade de salvar não implementada nesta demo."));
            load.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidade de carregar não implementada nesta demo."));
        }

        // Método principal para criar a interface do configurador
        private void createConfigurator() {
        
            JPanel left = new JPanel(new GridBagLayout());
            left.setBorder(BorderFactory.createTitledBorder("Opções do Carro"));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6, 6, 6, 6);
            c.fill = GridBagConstraints.HORIZONTAL;

            // Seleção de modelo
            c.gridx = 0; c.gridy = 0;
            left.add(new JLabel("Modelo:"), c);

            modelCombo = new JComboBox<>(new String[]{"City GT", "Sedan Lux", "SUV Ranger", "Sport Coupe"});
            c.gridx = 1; c.gridy = 0; c.gridwidth = 2;
            left.add(modelCombo, c);

            // Seleção de tipo de moto (usamos o RadioButton)
            c.gridwidth = 1; c.gridx = 0; c.gridy = 1;
            left.add(new JLabel("Motor:"), c);

            enginePetrol = new JRadioButton("Gasolina");
            engineHybrid = new JRadioButton("Híbrido");
            engineElectric = new JRadioButton("Elétrico");
            ButtonGroup bg = new ButtonGroup();
            bg.add(enginePetrol); bg.add(engineHybrid); bg.add(engineElectric);
            enginePetrol.setSelected(true);
            JPanel enginePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            enginePanel.add(enginePetrol); enginePanel.add(engineHybrid); enginePanel.add(engineElectric);
            c.gridx = 1; c.gridy = 1; c.gridwidth = 2;
            left.add(enginePanel, c);

            // Selção de acessórios (usamos checkBox)
            c.gridx = 0; c.gridy = 2; c.gridwidth = 1;
            left.add(new JLabel("Acessórios:"), c);

            sunroof = new JCheckBox("Teto Solar");
            leatherSeats = new JCheckBox("Bancos de Couro");
            sportsPack = new JCheckBox("Pacote Esportivo");
            gps = new JCheckBox("Navegação GPS");
            JPanel accPanel = new JPanel(new GridLayout(2, 2));
            accPanel.add(sunroof); accPanel.add(leatherSeats); accPanel.add(sportsPack); accPanel.add(gps);
            c.gridx = 1; c.gridy = 2; c.gridwidth = 2;
            left.add(accPanel, c);

            // Controle de potência (usamos JSlider)
            c.gridx = 0; c.gridy = 3; c.gridwidth = 1;
            left.add(new JLabel("Potência (HP):"), c);

            hpSlider = new JSlider(75, 650, 150); // Valores min, max e padrão
            hpSlider.setMajorTickSpacing(125);
            hpSlider.setPaintTicks(true);
            hpSlider.setPaintLabels(true);
            
            // Cria labels personalizadas para o slider
            Hashtable<Integer, JLabel> labels = new Hashtable<>();
            labels.put(75, new JLabel("75"));
            labels.put(250, new JLabel("250"));
            labels.put(375, new JLabel("375"));
            labels.put(500, new JLabel("500"));
            labels.put(650, new JLabel("650"));
            hpSlider.setLabelTable(labels);
            c.gridx = 1; c.gridy = 3; c.gridwidth = 2;
            left.add(hpSlider, c);

            // Label para mostrar o valor atual do HP
            hpValueLabel = new JLabel("150 HP");
            c.gridx = 3; c.gridy = 3; c.gridwidth = 1;
            left.add(hpValueLabel, c);

            // Botão para seleção de cor
            c.gridx = 0; c.gridy = 4;
            left.add(new JLabel("Cor:"), c);

            colorButton = new JButton("Selecionar Cor");
            c.gridx = 1; c.gridy = 4; c.gridwidth = 2;
            left.add(colorButton, c);

            // Seleção de rodas (usamos o JList)
            c.gridx = 0; c.gridy = 5; c.gridwidth = 1;
            left.add(new JLabel("Rodas:"), c);
            DefaultListModel<String> wheels = new DefaultListModel<>();
            wheels.addElement("Aro 16 - Standard");
            wheels.addElement("Aro 17 - Alloy");
            wheels.addElement("Aro 18 - Sport");
            wheels.addElement("Aro 19 - Premium");
            wheelList = new JList<>(wheels);
            wheelList.setSelectedIndex(1);
            JScrollPane wheelScroll = new JScrollPane(wheelList);
            wheelScroll.setPreferredSize(new Dimension(220, 64));
            c.gridx = 1; c.gridy = 5; c.gridwidth = 2;
            left.add(wheelScroll, c);

            // Controle de quantidade (usamos JSpinner)
            c.gridx = 0; c.gridy = 6; c.gridwidth = 1;
            left.add(new JLabel("Quantidade:"), c);
            quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            c.gridx = 1; c.gridy = 6;
            left.add(quantitySpinner, c);

            // Botão toggle para modo esportivo
            sportToggle = new JToggleButton("Modo Sport");
            c.gridx = 2; c.gridy = 6;
            left.add(sportToggle, c);

            //Área de texto para observações
            c.gridx = 0; c.gridy = 7; c.gridwidth = 1;
            left.add(new JLabel("Observações:"), c);
            notesArea = new JTextArea(4, 20);
            JScrollPane notesScroll = new JScrollPane(notesArea);
            c.gridx = 1; c.gridy = 7; c.gridwidth = 2;
            left.add(notesScroll, c);

            // Botões de ação
            applyButton = new JButton("Aplicar e Calcular Preço");
            checkoutButton = new JButton("Finalizar Compra");
            checkoutButton.setEnabled(false);
            backButton = new JButton("Voltar ao Menu");
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            buttons.add(applyButton); buttons.add(checkoutButton); buttons.add(backButton);
            c.gridx = 0; c.gridy = 8; c.gridwidth = 3;
            left.add(buttons, c);

            // Barra de progresso para simulação de cálculo
            calcProgress = new JProgressBar(0, 100);
            calcProgress.setValue(0);
            calcProgress.setStringPainted(true);
            c.gridx = 0; c.gridy = 9; c.gridwidth = 3;
            left.add(calcProgress, c);

            // Painel direito com preview do carro 
            JPanel right = new JPanel(new BorderLayout(8, 8));
            right.setBorder(BorderFactory.createTitledBorder("Preview do Carro"));

            // Label personalizada para desenhar o preview do carro
            previewLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(245, 245, 245));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(chosenColor);
                    int w = getWidth();
                    int h = getHeight();
                    g2.fillRoundRect(w/8, h/3, w*3/4, h/6, 40, 40);
                    g2.fillOval(w/6, h/2, w/6, h/6);
                    g2.fillOval(w*2/3, h/2, w/6, h/6);
                    g2.setColor(Color.DARK_GRAY);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                    g2.drawString((String) modelCombo.getSelectedItem(), 20, 24);
                }
            };
            previewLabel.setPreferredSize(new Dimension(420, 380));
            right.add(previewLabel, BorderLayout.CENTER);

            // Painel de resumo com preço e características
            JPanel summary = new JPanel(new GridBagLayout());
            summary.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            GridBagConstraints s = new GridBagConstraints();
            s.insets = new Insets(6, 6, 6, 6);
            s.gridx = 0; s.gridy = 0; s.anchor = GridBagConstraints.WEST;
            summary.add(new JLabel("Preço estimado:"), s);
            priceLabel = new JLabel("R$ 0,00");
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            s.gridx = 1; summary.add(priceLabel, s);

            s.gridx = 0; s.gridy = 1; s.gridwidth = 2;
            JLabel current = new JLabel("Características: —");
            summary.add(current, s);

            right.add(summary, BorderLayout.SOUTH);

            // Adiciona os painéis left e right ao frame principal
            add(left, BorderLayout.WEST);
            add(right, BorderLayout.CENTER);

            //Listener para os componentes interativos
            
            // Listener para o slider de HP
            hpSlider.addChangeListener(e -> {
                int hp = hpSlider.getValue();
                hpValueLabel.setText(hp + " HP");
                updatePreview();
            });

            // Listener para o botão de seleção de cor
            colorButton.addActionListener(e -> {
                Color chosen = JColorChooser.showDialog(this, "Escolher cor do carro", chosenColor);
                if (chosen != null) {
                    chosenColor = chosen;
                    updatePreview(); // Atualiza o preview com a nova cor
                }
            });

            // Listener para o botão de aplicar e calcular preço
            applyButton.addActionListener(e -> {
                // Executa em uma thread para a interface não travar 
                new Thread(() -> {
                    // Simula um processo de cálculo com barra de progresso
                    for (int i = 0; i <= 90; i += 10) {
                        try { Thread.sleep(40); } catch (InterruptedException ignored) {}
                        final int val = i;
                        // Atualiza a UI na EDT
                        SwingUtilities.invokeLater(() -> calcProgress.setValue(val));
                    }
                    currentPrice = calculatePrice();
                    // Atualiza a UI na EDT
                    SwingUtilities.invokeLater(() -> {
                        priceLabel.setText(String.format("R$ %,.2f", currentPrice));
                        calcProgress.setValue(100);
                        checkoutButton.setEnabled(true);
                        applySettings();
                    });
                }).start();
            });

            //Listener para o botão de finalizar compra
            checkoutButton.addActionListener(e -> {
                CustomerData customerData = new CustomerData(this, currentPrice);
                customerData.setVisible(true);
                this.setVisible(false);
            });

            backButton.addActionListener(e -> {
                this.setVisible(false);
                parent.setVisible(true);
            });

            // Listener genérico para atualizar o preview quando qualquer opção mudar
            ActionListener smallUpdater = e -> updatePreview();
            modelCombo.addActionListener(smallUpdater);
            enginePetrol.addActionListener(smallUpdater);
            engineHybrid.addActionListener(smallUpdater);
            engineElectric.addActionListener(smallUpdater);
            sunroof.addActionListener(smallUpdater);
            leatherSeats.addActionListener(smallUpdater);
            sportsPack.addActionListener(smallUpdater);
            gps.addActionListener(smallUpdater);
            wheelList.addListSelectionListener(e -> updatePreview());
            quantitySpinner.addChangeListener(e -> updatePreview());
            sportToggle.addActionListener(smallUpdater);

            updatePreview();
        }

        private void updatePreview() {
            previewLabel.repaint();

            // Constrói string com as caracteristicas selecionadas
            StringBuilder sb = new StringBuilder();
            sb.append(modelCombo.getSelectedItem()).append(" | ");
            if (enginePetrol.isSelected()) sb.append("Gasolina");
            else if (engineHybrid.isSelected()) sb.append("Híbrido");
            else sb.append("Elétrico");
            sb.append(" | ").append(hpSlider.getValue()).append(" HP");
            if (sunroof.isSelected()) sb.append(" | Teto");
            if (leatherSeats.isSelected()) sb.append(" | Couro");
            if (sportsPack.isSelected()) sb.append(" | SportPack");
            if (gps.isSelected()) sb.append(" | GPS");
            if (sportToggle.isSelected()) sb.append(" | ModoSport");

            // Atualiza a label de características no painel de resumo
            Container parent = priceLabel.getParent();
            if (parent instanceof JComponent) {
                Component[] comps = ((JComponent) parent).getComponents();
                if (comps.length >= 3 && comps[2] instanceof JLabel) {
                    ((JLabel) comps[2]).setText("Características: " + sb.toString());
                }
            }
        }

        // Método para calcular o preço com base nas seleções
        // Acréscimos
        private double calculatePrice() {
            double base = 60000; // preço base
            String model = (String) modelCombo.getSelectedItem();
            if (model.contains("City")) base = 45000;
            if (model.contains("Sedan")) base = 70000;
            if (model.contains("SUV")) base = 95000;
            if (model.contains("Sport")) base = 120000;

            if (engineHybrid.isSelected()) base += 12000;
            if (engineElectric.isSelected()) base += 30000;

            // Acréscimo por potência (cada 50 HP acima 150 acrescenta R$ 2500)
            int hp = hpSlider.getValue();
            base += ((hp - 150) / 50.0) * 2500.0;

            if (sunroof.isSelected()) base += 3500;
            if (leatherSeats.isSelected()) base += 5000;
            if (sportsPack.isSelected()) base += 8000;
            if (gps.isSelected()) base += 1200;

            String wheel = wheelList.getSelectedValue();
            if (wheel != null && wheel.contains("Premium")) base += 4200;
            else if (wheel != null && wheel.contains("Sport")) base += 2800;
            else if (wheel != null && wheel.contains("Alloy")) base += 1500;

            if (sportToggle.isSelected()) base += 2500;

            int qty = (Integer) quantitySpinner.getValue();
            base = base * qty;

            // Desconto para carros elétricos em quantidade
            if (engineElectric.isSelected() && qty > 1) base *= 0.97;

            return Math.max(base, 0.0);
        }

        // Método para mostrar resumo das configurações selecionadas
        private void applySettings() {
            StringBuilder sb = new StringBuilder();
            sb.append("Resumo:\n");
            sb.append("Modelo: ").append(modelCombo.getSelectedItem()).append('\n');
            sb.append("Motor: ").append(enginePetrol.isSelected() ? "Gasolina" : engineHybrid.isSelected() ? "Híbrido" : "Elétrico");
            sb.append('\n');
            sb.append("HP: ").append(hpSlider.getValue()).append('\n');
            sb.append("Acessórios: ");
            if (sunroof.isSelected()) sb.append("Teto Solar, ");
            if (leatherSeats.isSelected()) sb.append("Couro, ");
            if (sportsPack.isSelected()) sb.append("Pacote Esportivo, ");
            if (gps.isSelected()) sb.append("GPS, ");
            sb.append('\n');
            sb.append("Rodas: ").append(wheelList.getSelectedValue()).append('\n');
            sb.append("Quantidade: ").append(quantitySpinner.getValue()).append('\n');
            sb.append("Observações: ").append(notesArea.getText()).append('\n');

            JOptionPane.showMessageDialog(this, sb.toString(), "Resumo da Configuração", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Classe para a tela de dados do cliente
    static class CustomerData extends JFrame {
        private JFrame parent;
        private double totalPrice;
        
        private JTextField nameField, emailField, phoneField, addressField;
        private JComboBox<String> paymentMethod;
        private JRadioButton creditCard, debitCard, bankTransfer, cash;
        private JTextField cardNumberField, cardNameField, expiryField, cvvField;
        private JButton confirmButton, backButton;
        
        public CustomerData(JFrame parent, double totalPrice) {
            this.parent = parent;
            this.totalPrice = totalPrice;
            
            setTitle("Finalizar Compra - Dados do Cliente");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));
            
            createCustomerForm();
        }
        
        private void createCustomerForm() {
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6, 6, 6, 6);
            c.fill = GridBagConstraints.HORIZONTAL;
            
            JLabel title = new JLabel("Dados do Cliente e Pagamento", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 18));
            c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
            mainPanel.add(title, c);
            
            c.gridwidth = 1; c.gridy = 1;
            mainPanel.add(new JLabel("Nome Completo:"), c);
            nameField = new JTextField(20);
            c.gridx = 1; 
            mainPanel.add(nameField, c);
            
            c.gridx = 0; c.gridy = 2;
            mainPanel.add(new JLabel("E-mail:"), c);
            emailField = new JTextField(20);
            c.gridx = 1;
            mainPanel.add(emailField, c);
            
            c.gridx = 0; c.gridy = 3;
            mainPanel.add(new JLabel("Telefone:"), c);
            phoneField = new JTextField(20);
            c.gridx = 1;
            mainPanel.add(phoneField, c);
            
            c.gridx = 0; c.gridy = 4;
            mainPanel.add(new JLabel("Endereço:"), c);
            addressField = new JTextField(20);
            c.gridx = 1;
            mainPanel.add(addressField, c);
            
            c.gridx = 0; c.gridy = 5;
            mainPanel.add(new JLabel("Método de Pagamento:"), c);
            
            JPanel paymentPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            creditCard = new JRadioButton("Cartão de Crédito");
            debitCard = new JRadioButton("Cartão de Débito");
            bankTransfer = new JRadioButton("Transferência Bancária");
            cash = new JRadioButton("Dinheiro");

            // Grupo de botões para métodos de pagamentos (apenas um selecionado)
            ButtonGroup paymentGroup = new ButtonGroup();
            paymentGroup.add(creditCard);
            paymentGroup.add(debitCard);
            paymentGroup.add(bankTransfer);
            paymentGroup.add(cash);
            creditCard.setSelected(true);
            
            paymentPanel.add(creditCard);
            paymentPanel.add(debitCard);
            paymentPanel.add(bankTransfer);
            paymentPanel.add(cash);
            
            c.gridx = 1; c.gridy = 5;
            mainPanel.add(paymentPanel, c);

            // Detalhes do cartão (visível apenas para pagamento comm cartão)
            JPanel cardDetails = new JPanel(new GridBagLayout());
            cardDetails.setBorder(BorderFactory.createTitledBorder("Detalhes do Cartão"));
            GridBagConstraints cd = new GridBagConstraints();
            cd.insets = new Insets(4, 4, 4, 4);
            cd.fill = GridBagConstraints.HORIZONTAL;
            
            cd.gridx = 0; cd.gridy = 0;
            cardDetails.add(new JLabel("Número do Cartão:"), cd);
            cardNumberField = new JTextField(16);
            cd.gridx = 1;
            cardDetails.add(cardNumberField, cd);
            
            cd.gridx = 0; cd.gridy = 1;
            cardDetails.add(new JLabel("Nome no Cartão:"), cd);
            cardNameField = new JTextField(20);
            cd.gridx = 1;
            cardDetails.add(cardNameField, cd);
            
            cd.gridx = 0; cd.gridy = 2;
            cardDetails.add(new JLabel("Validade (MM/AA):"), cd);
            expiryField = new JTextField(5);
            cd.gridx = 1;
            cardDetails.add(expiryField, cd);
            
            cd.gridx = 0; cd.gridy = 3;
            cardDetails.add(new JLabel("CVV:"), cd);
            cvvField = new JTextField(3);
            cd.gridx = 1;
            cardDetails.add(cvvField, cd);
            
            c.gridx = 0; c.gridy = 6; c.gridwidth = 2;
            mainPanel.add(cardDetails, c);

            // Listener para mostrar//ocultar detalhes do cartão conforme método do pagamento
            ActionListener paymentListener = e -> {
                boolean cardSelected = creditCard.isSelected() || debitCard.isSelected();
                cardDetails.setVisible(cardSelected);
                revalidate();
                repaint();
            };
            
            creditCard.addActionListener(paymentListener);
            debitCard.addActionListener(paymentListener);
            bankTransfer.addActionListener(paymentListener);
            cash.addActionListener(paymentListener);

            // Label com o total a pagar
            JLabel totalLabel = new JLabel("Total a Pagar: R$ " + String.format("%,.2f", totalPrice));
            totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            totalLabel.setForeground(new Color(0, 100, 0));
            c.gridx = 0; c.gridy = 7; c.gridwidth = 2;
            mainPanel.add(totalLabel, c);
            
            confirmButton = new JButton("Confirmar Compra");
            backButton = new JButton("Voltar");
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.add(backButton);
            buttonPanel.add(confirmButton);
            c.gridx = 0; c.gridy = 8; c.gridwidth = 2;
            mainPanel.add(buttonPanel, c);
            
            add(mainPanel, BorderLayout.CENTER);
            
            backButton.addActionListener(e -> {
                this.setVisible(false);
                parent.setVisible(true);
            });
            
            confirmButton.addActionListener(e -> {
                if (validateForm()) {
                    JOptionPane.showMessageDialog(this, 
                        "Compra realizada com sucesso!\nObrigado pela preferência.", 
                        "Confirmação", 
                        JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0); // Encerra a aplicação
                }
            });
        }

        // Método para validar os dados do formulário
        private boolean validateForm() {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, informe seu nome.", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (emailField.getText().trim().isEmpty() || !emailField.getText().contains("@")) {
                JOptionPane.showMessageDialog(this, "Por favor, informe um e-mail válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validações específicas para pagamentos com cartão
            if (creditCard.isSelected() || debitCard.isSelected()) {
                if (cardNumberField.getText().trim().length() < 16) {
                    JOptionPane.showMessageDialog(this, "Número do cartão inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                if (cardNameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, informe o nome no cartão.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                if (expiryField.getText().trim().length() != 5 || !expiryField.getText().contains("/")) {
                    JOptionPane.showMessageDialog(this, "Data de validade inválida. Use o formato MM/AA.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                if (cvvField.getText().trim().length() != 3) {
                    JOptionPane.showMessageDialog(this, "CVV inválido. Deve conter 3 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            return true; // Todos os dados são validos
        }
    }
}