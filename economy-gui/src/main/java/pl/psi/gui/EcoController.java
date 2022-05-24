package pl.psi.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.SneakyThrows;
import pl.psi.EconomyEngine;
import pl.psi.artifacts.Artifact;
import pl.psi.artifacts.ArtifactPlacement;
import pl.psi.artifacts.EconomyArtifactFactory;
import pl.psi.converter.EcoBattleConverter;
import pl.psi.ProductType;
import pl.psi.creatures.EconomyCreature;
import pl.psi.creatures.EconomyNecropolisFactory;
import pl.psi.hero.EconomyHero;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import pl.psi.shop.BuyProductInterface;

import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public class EcoController implements PropertyChangeListener
{
    private final EconomyEngine economyEngine;
    @FXML
    ScrollPane heroBoughtScrollPane;
    @FXML
    ScrollPane skillsScrollPane;
    @FXML
    ScrollPane spellsScrollPane;
    @FXML
    StackPane heroBoughtPane;

    @FXML
    HBox shopsBox;
    @FXML
    Button readyButton;
    @FXML
    Label playerLabel;
    @FXML
    Label currentGoldLabel;
    @FXML
    Label GoldImage;
    @FXML
    Label roundNumberLabel;
    @FXML
    ScrollPane scrollPane;

    @FXML
    VBox VBoxHero;

    @FXML
    Button creaturete1;
    @FXML
    Button creaturete2;
    @FXML
    Button creaturete3;
    @FXML
    Button creaturete4;
    @FXML
    Button creaturete5;
    @FXML
    Button creaturete6;
    @FXML
    Button creaturete7;


    @FXML
    Button HEAD;
    @FXML
    Button TORSO;
    @FXML
    Button NECK;
    @FXML
    Button FEET;
    @FXML
    Button RIGHT_HAND;
    @FXML
    Button LEFT_HAND;
    @FXML
    Button SHOULDERS;


    private ProductType shopChoose;
    private HashMap<ArtifactPlacement,Button> artifactPlacementButtonHashMap;
    private List<Button> creatureButtons;
    private List<Button> artifactButtons;


    public EcoController( final EconomyHero aHero1, final EconomyHero aHero2 )
    {
        economyEngine = new EconomyEngine( aHero1, aHero2 );
        // default choose creatures
        shopChoose = ProductType.CREATURE;


    }

    @FXML
    void initialize() throws FileNotFoundException {

        artifactPlacementButtonHashMap = new HashMap<>();
        artifactPlacementButtonHashMap.put(ArtifactPlacement.HEAD,HEAD);
        artifactPlacementButtonHashMap.put(ArtifactPlacement.RIGHT_HAND,RIGHT_HAND);
        artifactPlacementButtonHashMap.put(ArtifactPlacement.LEFT_HAND,LEFT_HAND);
        artifactPlacementButtonHashMap.put(ArtifactPlacement.TORSO,TORSO);
        artifactPlacementButtonHashMap.put(ArtifactPlacement.NECK,NECK);
        artifactPlacementButtonHashMap.put(ArtifactPlacement.SHOULDERS,SHOULDERS);
        artifactPlacementButtonHashMap.put(ArtifactPlacement.FEET,FEET);

        creatureButtons = List.of(creaturete1,creaturete2,creaturete3,creaturete4,creaturete5,creaturete6,creaturete7);
        artifactButtons = List.of(HEAD,RIGHT_HAND,LEFT_HAND,FEET,NECK,TORSO,SHOULDERS);

        VBoxHero.setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));

        Image imageGold = new Image("GOLD.png");
        ImageView imageViewGold = new ImageView(imageGold);
        imageViewGold.setFitWidth(40);
        imageViewGold.setFitHeight(30);
        GoldImage.setGraphic(imageViewGold);

        // enable scroll Width/Height
        scrollPane.setFitToWidth(true);
        heroBoughtScrollPane.setFitToWidth(true);
        spellsScrollPane.setFitToHeight(true);
        skillsScrollPane.setFitToHeight(true);

        Image imageHero = new Image("HERO.png");
        ImageView imageView = new ImageView(imageHero);
        imageView.setFitWidth(350);
        imageView.setFitHeight(350);

        heroBoughtPane.setBackground(new Background(new BackgroundImage(imageHero, NO_REPEAT, NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        refreshGui();
        economyEngine.addObserver( EconomyEngine.ACTIVE_HERO_CHANGED, this );
        economyEngine.addObserver( EconomyEngine.HERO_BOUGHT_CREATURE, this );
        economyEngine.addObserver( EconomyEngine.HERO_BOUGHT_ARTIFACT, this );
        economyEngine.addObserver( EconomyEngine.NEXT_ROUND, this );
        economyEngine.addObserver( EconomyEngine.END_SHOPPING, this );


        readyButton.addEventHandler( MouseEvent.MOUSE_CLICKED, ( e ) -> {
            if( economyEngine.getRoundNumber() == 1 )
            {
                economyEngine.pass();
            }
            else if(economyEngine.getRoundNumber() == 2) {

                economyEngine.pass();
                goToBattle();
            }
        } );
    }

    private void goToBattle() {
        EcoBattleConverter.startBattle( economyEngine.getPlayer1(), economyEngine.getPlayer2() );
    }


    void refreshGui() throws FileNotFoundException {

        // refresh top of the scene - activeHero , Gold , RoundNumber
        playerLabel.setText( economyEngine.getActiveHero().toString() );
        currentGoldLabel.setText( String.valueOf( economyEngine.getActiveHero().getGold() ) );
        roundNumberLabel.setText( String.valueOf( economyEngine.getRoundNumber() ) );

        shopsBox.getChildren().clear();

        // refresh items in shop depends on what kind of products was chosen
        if(shopChoose.equals(ProductType.ARTIFACT)){
            fillShopWithArtifacts();
        }
        else if(shopChoose.equals(ProductType.CREATURE))
        {
            fillShopWithCreatures();
        }

        // refresh buttons of creatures for the hero2 - without it  hero2 can see what hero1 bought
        if(economyEngine.getRoundNumber()==2){
            for(int i=0;i<7;i++) {
                Button button = creatureButtons.get(i);
                Image image = new Image("CLEAR.png");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                button.setGraphic(imageView);
                button.setText("");
            }

            for(int i=0;i<7;i++){
                Button button = artifactButtons.get(i);
                Image image = new Image("CLEAR.png");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                button.setGraphic(imageView);

            }

        }

        //refresh boughtCreatures
        List<EconomyCreature> creatureList = economyEngine.getActiveHero().getCreatures();
        int numberOfBoughtCreatures = creatureList.size();
        for(int i=0;i<numberOfBoughtCreatures;i++) {

            EconomyCreature creature = creatureList.get(i);
            Button button = creatureButtons.get(i);
            int amount = creature.getAmount();
            int tier = creature.getTier();
            String upgrated = null;
            if(creature.isUpgraded())
                upgrated="1";
            else
                upgrated="0";
            String picture = tier + upgrated;

            button.setText(amount+"");
            Image image = new Image("/creatures/" + picture+ ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(30);
            imageView.setFitWidth(26);
            button.setGraphic(imageView);
            button.setContentDisplay(ContentDisplay.LEFT);

        }

        //refresh boughtArtifacts
        List<Artifact> artifacts = economyEngine.getActiveHero().getArtifacts();
        for(Map.Entry<ArtifactPlacement,Button> b :artifactPlacementButtonHashMap.entrySet()){
            ArtifactPlacement placement = b.getKey();
            for(Artifact a:artifacts){
                if(a.getPlacement().equals(placement)){
                    Button button =  b.getValue();
                    Image image = new Image("/artifacts/" + a.getName()+ ".png");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(39);
                    imageView.setFitWidth(39);
                    button.setGraphic(imageView);
                    button.setContentDisplay(ContentDisplay.CENTER);
                }
            }
        }
    }

    void buy(ProductType productType , final BuyProductInterface product )
    {
        economyEngine.buy( productType,product );
    }

    @SneakyThrows
    @Override
    public void propertyChange( final PropertyChangeEvent aPropertyChangeEvent )
    {
        refreshGui();
    }



    // Clicked section of Shop
    @FXML
    private void CreatureShopClicked(MouseEvent event){
        shopsBox.getChildren().clear();
        // refresh items in the store
        fillShopWithCreatures();
        shopChoose = ProductType.CREATURE;
    }

    @FXML
    private void ArtifactShopClicked(MouseEvent event){
        shopsBox.getChildren().clear();
        fillShopWithArtifacts();
        shopChoose = ProductType.ARTIFACT;

    }

    @FXML
    private void SkillsShopClicked(MouseEvent event){

    }

    @FXML
    private void SpellsShopClicked(MouseEvent event){

    }



    private void fillShopWithCreatures(){
        int gold = economyEngine.getActiveHero().getGold();
        final VBox creatureShop = new VBox();
        // refresh creatures
        final EconomyNecropolisFactory factory = new EconomyNecropolisFactory();
        for( int i = 1; i < 8; i++ )
        {
            EconomyCreature creature = factory.create(false,i,1);
            int costOfCreature= creature.getGoldCost().getProductPrice();
            int maxCreaturesHeroCanBuy = gold/costOfCreature;
            CreatureButton button = new CreatureButton( this, creature , maxCreaturesHeroCanBuy );
            String name = i + "0";
            Image image = new Image("/creatures/"+name+".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            button.setGraphic(imageView);
            button.setText(creature.getName());
            button.setContentDisplay(ContentDisplay.LEFT);
            // if hero cannot buy button will be grey
            if(gold < costOfCreature){
                button.getStyleClass().add("centerHBoxGrey");
            }
            else{
                button.getStyleClass().add("centerHBoxRight");
            }
            creatureShop.getChildren().add(button);

            EconomyCreature creature2 = factory.create(true,i,1);
            int costOfCreature2 = creature2.getGoldCost().getProductPrice();
            maxCreaturesHeroCanBuy = gold/costOfCreature2;
            CreatureButton button2 = new CreatureButton( this, creature2 ,maxCreaturesHeroCanBuy);
            String name2 = i + "1";
            Image image2 = new Image("/creatures/"+name2+".png");
            ImageView imageView2 = new ImageView(image2);
            imageView2.setFitHeight(40);
            imageView2.setFitWidth(40);
            button2.setGraphic(imageView2);
            button2.setText(creature2.getName());
            button2.setContentDisplay(ContentDisplay.LEFT);
            // if hero cannot buy button will be grey
            if(gold < costOfCreature2){
                button2.getStyleClass().add("centerHBoxGrey");
            }
            else{
                button2.getStyleClass().add("centerHBoxRight");
            }
            creatureShop.getChildren().add( button2 );
        }
        shopsBox.getChildren().add( creatureShop );
    }

    private void fillShopWithArtifacts(){
        final VBox creatureShop = new VBox();
        final EconomyArtifactFactory economyArtifactFactory = new EconomyArtifactFactory();
        List<String> namesOfArtifacts = List.of("Cape of Conjuring","Crown of Dragontooth","Blackshard of the Dead Knight");
        int size = namesOfArtifacts.size();
        int gold = economyEngine.getActiveHero().getGold();
        for(int i=0;i<size;i++){

            String name = namesOfArtifacts.get(i);
            Artifact artifact = economyArtifactFactory.create(name);
            int costOfArtifact = artifact.getGoldCost().getProductPrice();
            ArtifactButton button = new ArtifactButton(this,artifact);

            Image image = new Image("/artifacts/"+name+".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            button.setGraphic(imageView);
            button.setText(name);
            button.setContentDisplay(ContentDisplay.LEFT);
            // if hero cannot buy button will be grey
            if(gold < costOfArtifact){
                button.getStyleClass().add("centerHBoxGrey");
            }
            else{
                button.getStyleClass().add("centerHBoxRight");
            }
            button.getStyleClass().add("centerHBoxRight");

            creatureShop.getChildren().add(button);
        }
        shopsBox.getChildren().add( creatureShop );
    }


}
