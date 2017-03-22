package com.wisteca.quartzlegion.entities.personnages;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.MainClass;
import com.wisteca.quartzlegion.data.Accessor;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AttackPouvoir;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.ClasseSkill;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.effects.EffectInterface.Part;

public class Joueur extends Personnage implements Player {

	private static Accessor myAccess = Accessor.createNewAccessor();
	
	private Player myPlayer;
	private Document myDocument;
	private int mySerializeTime = 0;
	private Task myTask;
	
	private BossBar myEnergyBar;
	private Clan myClan;
	private Grade myGrade;
	private int myCurrentXp;
	private ItemStack[] myHotBar = new ItemStack[9];
	private boolean myAttackMenuOpen = false;
	
	public Joueur(Player player)
	{
		super(player.getUniqueId());
		
		myPlayer = player;
		myEnergyBar = Bukkit.createBossBar("ENERGIE", BarColor.BLUE, BarStyle.SOLID);
		myEnergyBar.addPlayer(myPlayer);
		myEnergyBar.setVisible(true);
		myTask = new Task();
		
		try {
			
			myDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			myDocument.appendChild(myDocument.createElement("joueur"));
		
			if(myAccess.isCreated(player.getUniqueId()))
			{	// le joueur s'est déjà connecté et il possède un champ dans la bdd
				deserialize(myAccess.getXML(player.getUniqueId()));
				serialize(myDocument.getDocumentElement());
				Bukkit.broadcastMessage("Connexion de " + myPlayer.getName() + ", désérialisation terminé !");
			}
			else
			{	// premiere connexion du joueur
				serialize(myDocument.getDocumentElement());
				myAccess.create(player.getUniqueId(), myDocument.getDocumentElement());
				Bukkit.broadcastMessage("Première arrivée de " + myPlayer.getName());
				// FAIRE LE TUTO POUR LES NOUVEAUX JOUEURS !
			}
			
			healthChanged();
			energyChanged();
			expChanged();
			
		} catch(ParserConfigurationException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		super.serialize(toWrite);
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		
		for(Weapon w : getWeapons())
			if(w != null)
				myPlayer.getInventory().addItem(w.toItemStack());
	}
	
	@Override
	protected void healthChanged()
	{
		super.healthChanged();
		sendMessage(getHealth() + " / " + getTemporarySkill(ClasseSkill.VIE_TOTALE) + " * 20 = " + getHealth() / getTemporarySkill(ClasseSkill.VIE_TOTALE) * 20);
		myPlayer.setHealth(getHealth() / getTemporarySkill(ClasseSkill.VIE_TOTALE) * 20);
	}

	@Override
	protected void energyChanged()
	{
		myEnergyBar.setProgress((double) getEnergy() / getTemporarySkill(ClasseSkill.ENERGIE_TOTALE));
	}
	
	@Override
	public double getMaxHealth()
	{
		return getTemporarySkill(ClasseSkill.VIE_TOTALE);
	}
	
	@Override
	public void damageInfo(int damages, Personnage target)
	{
		sendMessage(Channel.COMBAT, "Votre attaque a baissée de " + damages + " points de vie votre adversaire !");
	}
	
	@Override
	public Location getCurrentLocation(Part part)
	{
		switch(part)
		{
			case HEAD :
				return myPlayer.getEyeLocation();
			case CENTER :
				return myPlayer.getLocation().add(0, 0.9, 0);
			case RIGHT_SHOULDER :
				return myPlayer.getLocation();
			case LEFT_SHOULDER :
				return myPlayer.getLocation();
			case RIGHT_FOOT :
				return myPlayer.getLocation();
			case LEFT_FOOT :
				return myPlayer.getLocation();
		}
		return null;
	}
	
	@Override
	public void doTime()
	{
		super.doTime();
		
		mySerializeTime++;
		if(mySerializeTime > 6000)
		{
			mySerializeTime = 0;
			saveProgress();
		}
	}
	
	@Override
	public void onEvent(Event e)
	{
		if(e instanceof PlayerInteractEvent)
		{
			PlayerInteractEvent interact = (PlayerInteractEvent) e;
			
			if(isAttackMenuOpen())
				interact.setCancelled(true);
			
			if(interact.getAction().equals(Action.RIGHT_CLICK_AIR) || interact.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if(myPlayer.isSneaking() && isAttackMenuOpen() == false)
				{   // ouverture du menu
					for(Weapon w : getWeapons())
					{
						if(w == null)
							continue;
						
						if(w.toItemStack().isSimilar(interact.getItem()))
						{
							interact.setCancelled(true);
							openAttackMenu();
						}
					}
				}
				else if(isAttackMenuOpen())
				{
					PlayerInventory inv = getInventory();
					inv.setHeldItemSlot(inv.getHeldItemSlot() == 8 ? 0 : inv.getHeldItemSlot() + 1);
				}
			}
			else if(isAttackMenuOpen() && interact.getAction().equals(Action.LEFT_CLICK_AIR) || interact.getAction().equals(Action.LEFT_CLICK_BLOCK))
			{
				if(interact.getItem() == null)
					return;
				
				for(AttackPouvoir ap : getPouvoirs())  // utilisation d'un pouvoir
				{
					if(ap == null)
						continue;
					
					if(ap.getRepresentativeItem().toItemStack().isSimilar(interact.getItem()))
					{
						ap.launch();
						return;
					}
				}
				
				if(myPlayer.isSneaking() && interact.getItem().hasItemMeta() && interact.getItem().getItemMeta().getDisplayName().equals("§4Retour"))
					closeAttackMenu(); // fermeture du menu
			}
		}
	}
	
	/**
	 * Sauvegarde la progression du joueur dans le fichier xml ou la BDD.
	 */
	
	public void saveProgress()
	{
		try {
			
			sendMessage(Channel.INFO, "Sauvegarde de votre progression ...");
			serialize(myDocument.getDocumentElement());
			myAccess.setXML(myPlayer.getUniqueId(), myDocument.getDocumentElement());
		
		} catch(ParserConfigurationException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Méthode appelée quand le joueur se déconnecte.
	 */
	
	public void disconnect()
	{
		closeAttackMenu();
		saveProgress();
		myTask.cancel();
		myEnergyBar.removeAll();
		Bukkit.broadcastMessage("Déconnexion de " + myPlayer.getName());
	}
	
	public boolean isAttackMenuOpen()
	{
		return myAttackMenuOpen;
	}
	
	public void openAttackMenu()
	{
		openAttackMenu(getPouvoirs());
	}
	
	public void openAttackMenu(AttackPouvoir[] pouvoirs)
	{
		if(myAttackMenuOpen)
			return;
		
		myAttackMenuOpen = true;
		
		PlayerInventory inv = myPlayer.getInventory();
		ItemStack withoutPouvoir = new Item(ItemType.BARRIER, 1, false, "§cAucun pouvoir dans ce slot.", null, true, false).toItemStack();
		ItemStack back = new Item(ItemType.BED, 1, false, "§4Retour", null, true, false).toItemStack();
		
		for(int i = 0 ; i < 9 ; i++)
		{
			myHotBar[i] = inv.getItem(i);
			
			if(i == 8)
			{
				inv.setItem(i, back);
				return;
			}
			
			if(pouvoirs[i] != null)
				inv.setItem(i, pouvoirs[i].getRepresentativeItem().toItemStack());
			else
				inv.setItem(i, withoutPouvoir);
		}
	}
	
	public void closeAttackMenu()
	{
		for(int i = 0 ; i < 9 ; i++)
			myPlayer.getInventory().setItem(i, myHotBar[i]);
		
		myAttackMenuOpen = false;
	}
	
	public void setClan(Clan clan)
	{
		myClan = clan;
	}
	
	public Clan getClan()
	{
		return myClan;
	}
	
	public void setGrade(Grade grade)
	{
		myGrade = grade;
	}
	
	public Grade getGrade()
	{
		return myGrade;
	}
	
	public void showEnergyBar(boolean show)
	{
		myEnergyBar.setVisible(show);
	}
	
	public boolean isShowingEnergyBar()
	{
		return myEnergyBar.isVisible();
	}
	
	public void setChannel(Channel ch)
	{
		
	}
	
	public Channel getChannel()
	{
		return null;
	}
	
	public void changeSkillPoints(int points)
	{
		
	}
	
	public int getSkillPoints()
	{
		return 0;
	}
	
	public int getSkillPointsCost(Skill skill)
	{
		return 0;
	}
	
	@Override
	protected void die()
	{
		sendMessage(Channel.INFO, "Vous êtes mort !");
		changeHealth((int) getMaxHealth());
	}
	
	@Override
	public void sendMessage(String message)
	{
		myPlayer.sendMessage(message);
	}

	@Override
	public void sendMessage(String[] messages)
	{
		myPlayer.sendMessage(messages);
	}
	
	private class Task extends BukkitRunnable {
		
		public Task()
		{
			this.runTaskTimer(MainClass.getInstance(), 1, 1);
		}
		
		@Override
		public void run()
		{
			doTime();
		}
	}
	
	public static enum Grade {
		
		Inconnue,
		Vagabond,
		Citoyen,
		Citadin;
	}
	
	public static enum Clan {
	
		Aucun, 
		Conquistador, 
		Pirate,
		Rebelle;
	}
	
	private void expChanged()
	{
		myPlayer.setLevel(getLevel());
		myPlayer.setExp((float) myCurrentXp / getExpBarSize());
	}
	
	@Override
	protected void levelChanged(int oldLevel, int levelsNumber)
	{
		if(getLevel() > oldLevel)
		{
			sendMessage(Channel.INFO, "§2Vous êtes passé du niveau " + oldLevel + " au niveau " + getLevel() + " ! +" + (20 * levelsNumber) + " points de compétences.");
			changeSkillPoints(20 * levelsNumber);
		}
	}
	
	/**
	 * @return le nombre d'xp nécessaire à un joueur pour remplir sa barre d'xp et changer de niveau
	 */
	
	public int getExpBarSize()
	{
		return getLevel() * 5;
	}
	
	/**
	 * @return le nombre d'xp nécessaire pour changer de niveau
	 */
	
	@Override
	public int getExpToLevel()
	{
		return getExpBarSize() - myCurrentXp;
	}
	
	/**
	 * Cette méthode ne fait rien, utilisez giveExp pour changer l'xp du joueur.
	 */
	
	@Override
	public void setExp(float arg0)
	{}
	
	/**
	 * @return l'xp du joueur
	 */
	
	@Override
	public float getExp()
	{
		return myCurrentXp;
	}
	
	/**
	 * Donner tant d'xp au joueur.
	 * @param xp l'xp à donner
	 */
	
	@Override
	public void giveExp(int xp)
	{
		if(xp < 0)
			return;
		
		int levelsToAdd = 0;
		for(int i = 0 ; i < xp ; i++)
		{
			myCurrentXp += 1;
			if(myCurrentXp > getExpBarSize())
			{
				levelsToAdd++;
				myCurrentXp = 0;
			}
		}
		
		setLevel(getLevel() + levelsToAdd);
		expChanged();
	}

	/**
	 * Donner des niveaux à un joueur.
	 * @param levels le nombre de niveaux à ajouter
	 */
	
	@Override
	public void giveExpLevels(int levels)
	{
		setLevel(getLevel() + levels);
		expChanged();
	}
	
	/**
	 * Méthode inutile, utilisez giveExp ou giveExpLevels pour changer l'xp du joueur.
	 */
	
	@Override
	public void setTotalExperience(int arg0)
	{}
	
	/**
	 * @return l'xp total du joueur, en partant du niveau 0, avec 0 xp
	 */
	
	@Override
	public int getTotalExperience()
	{
		int total = 0;
		for(int i = 1 ; i <= getLevel() ; i++)
			total += 5 * i;
		
		return total + myCurrentXp;
	}

	/**
	 * Méthode inutile.
	 */
	
	@Override
	public void resetMaxHealth()
	{}

	/**
	 * Méthode inutile, utilisez changeSkillFix(ClasseSkill.VIE_TOTALE, changement);
	 */
	
	@Override
	public void setMaxHealth(double arg0)
	{}

	/**
	 * Sérialisation bukkit, il est préférable d'utiliser la sérialisation XML.
	 */
	
	@Override
	public Map<String, Object> serialize()
	{
		return myPlayer.serialize();
	}

	/**
	 * @return getTemporarySkill(ClasseSkill.VITESSE_MARCHE); (retourne un int)
	 */
	
	@Override
	public float getWalkSpeed()
	{
		return getTemporarySkill(ClasseSkill.VITESSE_MARCHE);
	}
	
	/**
	 * Méthode inutile, utilisez changeSkillFix(ClasseSkill.VITESSE_MARCHE, int);
	 */
	
	@Override
	public void setWalkSpeed(float arg0) throws IllegalArgumentException
	{}
	
	@Override
	public Player getKiller()
	{
		return (Player) PersonnageManager.getInstance().getPersonnage(myPlayer.getKiller().getUniqueId());
	}
	
	/**
	 * Méthode inutile, utilisez damage(Damage damage);
	 */
	
	@Override
	public void damage(double arg0)
	{}

	/**
	 * Méthode inutile, utilisez damage(Damage damage);
	 */
	
	@Override
	public void damage(double arg0, Entity arg1)
	{}
	
	/*
	 *  Méthodes bukkit non-changées
	 */
	
	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z)
	{
		return myPlayer.getNearbyEntities(x, y, z);
	}
	
	@Override
	public double getHealthScale()
	{
		return myPlayer.getHealthScale();
	}

	@Override
	public boolean isHealthScaled()
	{
		return myPlayer.isHealthScaled();
	}

	@Override
	public void setHealthScale(double scale) throws IllegalArgumentException
	{
		myPlayer.setHealthScale(scale);
	}

	@Override
	public void setHealthScaled(boolean scaled)
	{
		myPlayer.setHealthScaled(scaled);
	}
	
	@Override
	public void setFoodLevel(int level)
	{
		myPlayer.setFoodLevel(level);
	}
	
	@Override
	public boolean addScoreboardTag(String tag)
	{
		return myPlayer.addScoreboardTag(tag);
	}

	@Override
	public boolean eject()
	{
		return myPlayer.eject();
	}

	@Override
	public String getCustomName()
	{
		return myPlayer.getCustomName();
	}

	@Override
	public int getEntityId()
	{
		return myPlayer.getEntityId();
	}

	@Override
	public float getFallDistance()
	{
		return myPlayer.getFallDistance();
	}

	@Override
	public int getFireTicks()
	{
		return myPlayer.getFireTicks();
	}

	@Override
	public EntityDamageEvent getLastDamageCause()
	{
		return myPlayer.getLastDamageCause();
	}

	@Override
	public Location getLocation()
	{
		return myPlayer.getLocation();
	}

	@Override
	public Location getLocation(Location loc)
	{
		return myPlayer.getLocation(loc);
	}

	@Override
	public int getMaxFireTicks()
	{
		return myPlayer.getMaxFireTicks();
	}

	@Deprecated
	@Override
	public Entity getPassenger()
	{
		return myPlayer.getPassenger();
	}

	@Override
	public int getPortalCooldown()
	{
		return myPlayer.getPortalCooldown();
	}

	@Override
	public Set<String> getScoreboardTags()
	{
		return myPlayer.getScoreboardTags();
	}

	@Override
	public Server getServer()
	{
		return myPlayer.getServer();
	}

	@Override
	public int getTicksLived()
	{
		return myPlayer.getTicksLived();
	}

	@Override
	public EntityType getType()
	{
		return myPlayer.getType();
	}

	@Override
	public Entity getVehicle()
	{
		return myPlayer.getVehicle();
	}

	@Override
	public Vector getVelocity()
	{
		return myPlayer.getVelocity();
	}

	@Override
	public World getWorld()
	{
		return myPlayer.getWorld();
	}

	@Override
	public boolean hasGravity()
	{
		return myPlayer.hasGravity();
	}

	@Override
	public boolean isCustomNameVisible()
	{
		return myPlayer.isCustomNameVisible();
	}

	@Override
	public boolean isEmpty()
	{
		return myPlayer.isEmpty();
	}

	@Override
	public boolean isGlowing()
	{
		return myPlayer.isGlowing();
	}

	@Override
	public boolean isInsideVehicle()
	{
		return myPlayer.isInsideVehicle();
	}

	@Override
	public boolean isInvulnerable()
	{
		return myPlayer.isInvulnerable();
	}

	@Deprecated
	@Override
	public boolean isOnGround()
	{
		return myPlayer.isOnGround();
	}

	@Override
	public boolean isSilent()
	{
		return myPlayer.isSilent();
	}

	@Override
	public boolean isValid()
	{
		return myPlayer.isValid();
	}

	@Override
	public boolean leaveVehicle()
	{
		return myPlayer.leaveVehicle();
	}

	@Override
	public void playEffect(EntityEffect effect)
	{
		myPlayer.playEffect(effect);
	}

	@Override
	public void remove()
	{
		myPlayer.remove();
	}

	@Override
	public boolean removeScoreboardTag(String tag)
	{
		return myPlayer.removeScoreboardTag(tag);
	}

	@Override
	public void setCustomName(String customName)
	{
		myPlayer.setCustomName(customName);
	}

	@Override
	public void setCustomNameVisible(boolean visible)
	{
		myPlayer.setCustomNameVisible(visible);
	}

	@Override
	public void setFallDistance(float distance)
	{
		myPlayer.setFallDistance(distance);
	}

	@Override
	public void setFireTicks(int fireTicks)
	{
		myPlayer.setFireTicks(fireTicks);
	}

	@Override
	public void setGlowing(boolean glowing)
	{
		myPlayer.setGlowing(glowing);
	}

	@Override
	public void setGravity(boolean gravity)
	{
		myPlayer.setGravity(gravity);
	}

	@Override
	public void setInvulnerable(boolean invulnerable)
	{
		myPlayer.setInvulnerable(invulnerable);
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent e)
	{
		myPlayer.setLastDamageCause(e);
	}

	@Deprecated
	@Override
	public boolean setPassenger(Entity passenger)
	{
		return myPlayer.setPassenger(passenger);
	}

	@Override
	public void setPortalCooldown(int ticks)
	{
		myPlayer.setPortalCooldown(ticks);
	}

	@Override
	public void setSilent(boolean silent)
	{
		myPlayer.setSilent(silent);
	}

	@Override
	public void setTicksLived(int ticks)
	{
		myPlayer.setTicksLived(ticks);
	}

	@Override
	public void setVelocity(Vector vector)
	{
		myPlayer.setVelocity(vector);
	}

	@Override
	public boolean teleport(Location loc)
	{
		return myPlayer.teleport(loc);
	}

	@Override
	public boolean teleport(Entity entity)
	{
		return myPlayer.teleport(entity);
	}

	@Override
	public boolean teleport(Location loc, TeleportCause cause)
	{
		return myPlayer.teleport(loc, cause);
	}

	@Override
	public boolean teleport(Entity entity, TeleportCause cause)
	{
		return myPlayer.teleport(entity, cause);
	}

	@Override
	public List<MetadataValue> getMetadata(String data)
	{
		return myPlayer.getMetadata(data);
	}

	@Override
	public boolean hasMetadata(String data)
	{
		return myPlayer.hasMetadata(data);
	}

	@Override
	public void removeMetadata(String data, Plugin plugin)
	{
		myPlayer.removeMetadata(data, plugin);
	}

	@Override
	public void setMetadata(String data, MetadataValue value)
	{
		myPlayer.setMetadata(data, value);
	}

	@Override
	public String getName()
	{
		return myPlayer.getName();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin)
	{
		return myPlayer.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks)
	{
		return myPlayer.addAttachment(plugin, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value)
	{
		return myPlayer.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks)
	{
		return myPlayer.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions()
	{
		return myPlayer.getEffectivePermissions();
	}

	@Override
	public boolean hasPermission(String permission)
	{
		return myPlayer.hasPermission(permission);
	}

	@Override
	public boolean hasPermission(Permission permission)
	{
		return myPlayer.hasPermission(permission);
	}

	@Override
	public boolean isPermissionSet(String permission)
	{
		return myPlayer.isPermissionSet(permission);
	}

	@Override
	public boolean isPermissionSet(Permission permission)
	{
		return myPlayer.isPermissionSet(permission);
	}

	@Override
	public void recalculatePermissions()
	{
		myPlayer.recalculatePermissions();
	}

	@Override
	public void removeAttachment(PermissionAttachment attachement)
	{
		myPlayer.removeAttachment(attachement);
	}

	@Override
	public boolean isOp()
	{
		return myPlayer.isOp();
	}

	@Override
	public void setOp(boolean op)
	{
		myPlayer.setOp(op);
	}

	@Override
	public Location getEyeLocation()
	{
		return myPlayer.getEyeLocation();
	}

	@Override
	public boolean addPassenger(Entity passenger)
	{
		return myPlayer.addPassenger(passenger);
	}

	@Override
	public List<Entity> getPassengers()
	{
		return myPlayer.getPassengers();
	}

	@Override
	public boolean removePassenger(Entity passenger)
	{
		return myPlayer.removePassenger(passenger);
	}

	@Override
	public void closeInventory()
	{
		myPlayer.closeInventory();
	}

	@Override
	public Inventory getEnderChest()
	{
		return myPlayer.getEnderChest();
	}

	@Override
	public GameMode getGameMode()
	{
		return myPlayer.getGameMode();
	}

	@Override
	public PlayerInventory getInventory()
	{
		return myPlayer.getInventory();
	}
	
	@Deprecated
	@Override
	public ItemStack getItemInHand()
	{
		return myPlayer.getItemInHand();
	}

	@Override
	public ItemStack getItemOnCursor()
	{
		return myPlayer.getItemOnCursor();
	}

	@Override
	public MainHand getMainHand()
	{
		return myPlayer.getMainHand();
	}

	@Override
	public InventoryView getOpenInventory()
	{
		return myPlayer.getOpenInventory();
	}

	@Override
	public int getSleepTicks()
	{
		return myPlayer.getSleepTicks();
	}

	@Override
	public boolean isBlocking()
	{
		return myPlayer.isBlocking();
	}

	@Override
	public boolean isHandRaised()
	{
		return myPlayer.isHandRaised();
	}

	@Override
	public boolean isSleeping()
	{
		return myPlayer.isSleeping();
	}

	@Override
	public InventoryView openEnchanting(Location loc, boolean force)
	{
		return myPlayer.openEnchanting(loc, force);
	}

	@Override
	public InventoryView openInventory(Inventory inv)
	{
		return myPlayer.openInventory(inv);
	}

	@Override
	public void openInventory(InventoryView inv)
	{
		myPlayer.openInventory(inv);
	}

	@Override
	public InventoryView openMerchant(Villager trader, boolean force)
	{
		return myPlayer.openMerchant(trader, force);
	}

	@Override
	public InventoryView openMerchant(Merchant merchant, boolean force)
	{
		return myPlayer.openMerchant(merchant, force);
	}

	@Override
	public InventoryView openWorkbench(Location loc, boolean force)
	{
		return myPlayer.openWorkbench(loc, force);
	}

	@Override
	public void setGameMode(GameMode mode)
	{
		myPlayer.setGameMode(mode);
	}

	@Deprecated
	@Override
	public void setItemInHand(ItemStack item)
	{
		myPlayer.setItemInHand(item);
	}

	@Override
	public void setItemOnCursor(ItemStack item)
	{
		myPlayer.setItemOnCursor(item);
	}

	@Override
	public boolean setWindowProperty(Property property, int value)
	{
		return myPlayer.setWindowProperty(property, value);
	}

	@Deprecated
	@Override
	public int _INVALID_getLastDamage()
	{
		return myPlayer._INVALID_getLastDamage();
	}
	
	@Deprecated
	@Override
	public void _INVALID_setLastDamage(int damages)
	{
		myPlayer._INVALID_setLastDamage(damages);
	}

	@Override
	public boolean addPotionEffect(PotionEffect effect)
	{
		return myPlayer.addPotionEffect(effect);
	}

	@Override
	public boolean addPotionEffect(PotionEffect effect, boolean force)
	{
		return myPlayer.addPotionEffect(effect, force);
	}

	@Override
	public boolean addPotionEffects(Collection<PotionEffect> effects)
	{
		return myPlayer.addPotionEffects(effects);
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects()
	{
		return myPlayer.getActivePotionEffects();
	}

	@Override
	public boolean getCanPickupItems()
	{
		return myPlayer.getCanPickupItems();
	}

	@Override
	public EntityEquipment getEquipment()
	{
		return myPlayer.getEquipment();
	}

	@Override
	public double getEyeHeight()
	{
		return myPlayer.getEyeHeight();
	}

	@Override
	public double getEyeHeight(boolean ignoreSneaking)
	{
		return myPlayer.getEyeHeight(ignoreSneaking);
	}
	
	@Override
	public double getLastDamage()
	{
		return myPlayer.getLastDamage();
	}

	@Deprecated
	@Override
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance)
	{
		return myPlayer.getLastTwoTargetBlocks(transparent, maxDistance);
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance)
	{
		return myPlayer.getLastTwoTargetBlocks(transparent, maxDistance);
	}

	@Override
	public Entity getLeashHolder() throws IllegalStateException
	{
		return myPlayer.getLeashHolder();
	}

	@Deprecated
	@Override
	public List<Block> getLineOfSight(HashSet<Byte> tranparent, int maxDistance)
	{
		return myPlayer.getLineOfSight(tranparent, maxDistance);
	}

	@Override
	public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance)
	{
		return myPlayer.getLastTwoTargetBlocks(transparent, maxDistance);
	}

	@Override
	public int getMaximumAir()
	{
		return myPlayer.getMaximumAir();
	}

	@Override
	public int getMaximumNoDamageTicks()
	{
		return myPlayer.getMaximumNoDamageTicks();
	}

	@Override
	public int getNoDamageTicks()
	{
		return myPlayer.getNoDamageTicks();
	}

	@Override
	public PotionEffect getPotionEffect(PotionEffectType effect)
	{
		return myPlayer.getPotionEffect(effect);
	}

	@Override
	public int getRemainingAir()
	{
		return myPlayer.getRemainingAir();
	}

	@Override
	public boolean getRemoveWhenFarAway()
	{
		return myPlayer.getRemoveWhenFarAway();
	}

	@Deprecated
	@Override
	public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance)
	{
		return myPlayer.getTargetBlock(transparent, maxDistance);
	}

	@Override
	public Block getTargetBlock(Set<Material> transparent, int maxDistance)
	{
		return myPlayer.getTargetBlock(transparent, maxDistance);
	}

	@Override
	public boolean hasAI()
	{
		return myPlayer.hasAI();
	}

	@Override
	public boolean hasLineOfSight(Entity ent)
	{
		return myPlayer.hasLineOfSight(ent);
	}

	@Override
	public boolean hasPotionEffect(PotionEffectType effect)
	{
		return myPlayer.hasPotionEffect(effect);
	}

	@Override
	public boolean isCollidable()
	{
		return myPlayer.isCollidable();
	}

	@Override
	public boolean isGliding()
	{
		return myPlayer.isGliding();
	}

	@Override
	public boolean isLeashed()
	{
		return myPlayer.isLeashed();
	}

	@Override
	public void removePotionEffect(PotionEffectType effect)
	{
		myPlayer.removePotionEffect(effect);
	}

	@Override
	public void setAI(boolean ai)
	{
		myPlayer.setAI(ai);
	}

	@Override
	public void setCanPickupItems(boolean pickup)
	{
		myPlayer.setCanPickupItems(pickup);
	}

	@Override
	public void setCollidable(boolean collidable)
	{
		myPlayer.setCollidable(collidable);
	}

	@Override
	public void setGliding(boolean gliding)
	{
		myPlayer.setGliding(gliding);
	}

	@Override
	public void setLastDamage(double lastDamage)
	{
		myPlayer.setLastDamage(lastDamage);
	}

	@Override
	public boolean setLeashHolder(Entity ent)
	{
		return myPlayer.setLeashHolder(ent);
	}

	@Override
	public void setMaximumAir(int air)
	{
		myPlayer.setMaximumAir(air);
	}

	@Override
	public void setMaximumNoDamageTicks(int ticks)
	{
		myPlayer.setMaximumNoDamageTicks(ticks);
	}

	@Override
	public void setNoDamageTicks(int ticks)
	{
		myPlayer.setNoDamageTicks(ticks);
	}

	@Override
	public void setRemainingAir(int air)
	{
		myPlayer.setRemainingAir(air);
	}

	@Override
	public void setRemoveWhenFarAway(boolean remove)
	{
		myPlayer.setRemoveWhenFarAway(remove);
	}

	@Override
	public AttributeInstance getAttribute(Attribute attr)
	{
		return myPlayer.getAttribute(attr);
	}

	@Deprecated
	@Override
	public void _INVALID_damage(int arg0)
	{
		myPlayer._INVALID_damage(arg0);
	}

	@Deprecated
	@Override
	public void _INVALID_damage(int arg0, Entity arg1)
	{
		myPlayer._INVALID_damage(arg0, arg1);
	}

	@Deprecated
	@Override
	public int _INVALID_getHealth()
	{
		return myPlayer._INVALID_getHealth();
	}

	@Deprecated
	@Override
	public int _INVALID_getMaxHealth()
	{
		return myPlayer._INVALID_getMaxHealth();
	}

	@Deprecated
	@Override
	public void _INVALID_setHealth(int arg0)
	{
		myPlayer._INVALID_setHealth(arg0);
	}

	@Deprecated
	@Override
	public void _INVALID_setMaxHealth(int arg0)
	{
		myPlayer._INVALID_setMaxHealth(arg0);
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile)
	{
		return myPlayer.launchProjectile(projectile);
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector vector)
	{
		return myPlayer.launchProjectile(projectile, vector);
	}

	@Override
	public void abandonConversation(Conversation conversation)
	{
		myPlayer.abandonConversation(conversation);
	}

	@Override
	public void abandonConversation(Conversation conversation, ConversationAbandonedEvent e)
	{
		myPlayer.abandonConversation(conversation, e);
	}

	@Override
	public void acceptConversationInput(String input)
	{
		myPlayer.acceptConversationInput(input);
	}

	@Override
	public boolean beginConversation(Conversation conversation)
	{
		return myPlayer.beginConversation(conversation);
	}

	@Override
	public boolean isConversing()
	{
		return myPlayer.isConversing();
	}

	@Override
	public long getFirstPlayed()
	{
		return myPlayer.getFirstPlayed();
	}

	@Override
	public long getLastPlayed()
	{
		return myPlayer.getLastPlayed();
	}

	@Override
	public boolean hasPlayedBefore()
	{
		return myPlayer.hasPlayedBefore();
	}

	@Override
	public boolean isBanned()
	{
		return myPlayer.isBanned();
	}

	@Override
	public boolean isOnline()
	{
		return myPlayer.isOnline();
	}

	@Override
	public boolean isWhitelisted()
	{
		return myPlayer.isWhitelisted();
	}

	@Deprecated
	@Override
	public void setBanned(boolean banned)
	{
		myPlayer.setBanned(banned);
	}

	@Override
	public void setWhitelisted(boolean whitelisted)
	{
		myPlayer.setWhitelisted(whitelisted);
	}
	
	@Override
	public Set<String> getListeningPluginChannels()
	{
		return myPlayer.getListeningPluginChannels();
	}

	@Override
	public void sendPluginMessage(Plugin plugin, String channel, byte[] message)
	{
		myPlayer.sendPluginMessage(plugin, channel, message);
	}

	@Override
	public void awardAchievement(Achievement achievement)
	{
		myPlayer.awardAchievement(achievement);
	}

	@Override
	public boolean canSee(Player p)
	{
		return myPlayer.canSee(p);
	}

	@Override
	public void chat(String message)
	{
		myPlayer.chat(message);
	}

	@Override
	public void decrementStatistic(Statistic statistic) throws IllegalArgumentException
	{
		myPlayer.decrementStatistic(statistic);
	}

	@Override
	public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException
	{
		myPlayer.decrementStatistic(statistic, amount);
	}

	@Override
	public void decrementStatistic(Statistic statistic, Material type) throws IllegalArgumentException
	{
		myPlayer.decrementStatistic(statistic, type);
	}

	@Override
	public void decrementStatistic(Statistic statistic, EntityType type) throws IllegalArgumentException
	{
		myPlayer.decrementStatistic(statistic, type);
	}

	@Override
	public void decrementStatistic(Statistic statistic, Material type, int amount) throws IllegalArgumentException
	{
		myPlayer.decrementStatistic(statistic, type, amount);
	}

	@Override
	public void decrementStatistic(Statistic statistic, EntityType type, int amount)
	{
		myPlayer.decrementStatistic(statistic, type, amount);
	}

	@Override
	public InetSocketAddress getAddress()
	{
		return myPlayer.getAddress();
	}

	@Override
	public boolean getAllowFlight()
	{
		return myPlayer.getAllowFlight();
	}

	@Override
	public Location getBedSpawnLocation()
	{
		return myPlayer.getBedSpawnLocation();
	}

	@Override
	public Location getCompassTarget()
	{
		return myPlayer.getCompassTarget();
	}

	@Override
	public String getDisplayName()
	{
		return myPlayer.getDisplayName();
	}

	@Override
	public float getExhaustion()
	{
		return myPlayer.getExhaustion();
	}
	
	@Override
	public float getFlySpeed()
	{
		return myPlayer.getFlySpeed();
	}

	@Override
	public int getFoodLevel()
	{
		return myPlayer.getFoodLevel();
	}

	@Override
	public String getPlayerListName()
	{
		return myPlayer.getPlayerListName();
	}

	@Override
	public long getPlayerTime()
	{
		return myPlayer.getPlayerTime();
	}

	@Override
	public long getPlayerTimeOffset()
	{
		return myPlayer.getPlayerTimeOffset();
	}

	@Override
	public WeatherType getPlayerWeather()
	{
		return myPlayer.getPlayerWeather();
	}

	@Override
	public float getSaturation()
	{
		return myPlayer.getSaturation();
	}

	@Override
	public Scoreboard getScoreboard()
	{
		return myPlayer.getScoreboard();
	}

	@Override
	public Entity getSpectatorTarget()
	{
		return myPlayer.getSpectatorTarget();
	}

	@Override
	public int getStatistic(Statistic statistic) throws IllegalArgumentException
	{
		return myPlayer.getStatistic(statistic);
	}

	@Override
	public int getStatistic(Statistic statistic, Material type) throws IllegalArgumentException
	{
		return myPlayer.getStatistic(statistic, type);
	}

	@Override
	public int getStatistic(Statistic statistic, EntityType type) throws IllegalArgumentException
	{
		return myPlayer.getStatistic(statistic, type);
	}

	@Override
	public boolean hasAchievement(Achievement achievement)
	{
		return myPlayer.hasAchievement(achievement);
	}

	@Override
	public void hidePlayer(Player p)
	{
		myPlayer.hidePlayer(p);
	}

	@Override
	public void incrementStatistic(Statistic statistic) throws IllegalArgumentException
	{
		myPlayer.incrementStatistic(statistic);
	}

	@Override
	public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException
	{
		myPlayer.incrementStatistic(statistic, amount);
	}

	@Override
	public void incrementStatistic(Statistic statistic, Material type) throws IllegalArgumentException
	{
		myPlayer.incrementStatistic(statistic, type);
	}

	@Override
	public void incrementStatistic(Statistic statistic, EntityType type) throws IllegalArgumentException
	{
		myPlayer.incrementStatistic(statistic, type);
	}

	@Override
	public void incrementStatistic(Statistic statistic, Material type, int amount) throws IllegalArgumentException
	{
		myPlayer.incrementStatistic(statistic, type, amount);
	}

	@Override
	public void incrementStatistic(Statistic statistic, EntityType type, int amount) throws IllegalArgumentException
	{
		myPlayer.incrementStatistic(statistic, type, amount);
	}

	@Override
	public boolean isFlying()
	{
		return myPlayer.isFlying();
	}

	@Override
	public boolean isPlayerTimeRelative()
	{
		return myPlayer.isPlayerTimeRelative();
	}

	@Override
	public boolean isSleepingIgnored()
	{
		return myPlayer.isSleepingIgnored();
	}

	@Override
	public boolean isSneaking()
	{
		return myPlayer.isSneaking();
	}

	@Override
	public boolean isSprinting()
	{
		return myPlayer.isSprinting();
	}

	@Override
	public void kickPlayer(String reason)
	{
		myPlayer.kickPlayer(reason);
	}

	@Override
	public void loadData()
	{
		myPlayer.loadData();
	}

	@Override
	public boolean performCommand(String command)
	{
		return myPlayer.performCommand(command);
	}

	@Deprecated
	@Override
	public void playEffect(Location loc, Effect effect, int data)
	{
		myPlayer.playEffect(loc, effect, data);
	}

	@Override
	public <T> void playEffect(Location loc, Effect effect, T data)
	{
		myPlayer.playEffect(loc, effect, data);
	}

	@Deprecated
	@Override
	public void playNote(Location loc, byte instrument, byte note)
	{
		myPlayer.playNote(loc, instrument, note);
	}

	@Override
	public void playNote(Location loc, Instrument instrument, Note note)
	{
		myPlayer.playNote(loc, instrument, note);
	}

	@Override
	public void playSound(Location loc, Sound sound, float volume, float pitch)
	{
		myPlayer.playSound(loc, sound, volume, pitch);
	}

	@Override
	public void playSound(Location loc, String sound, float volume, float pitch)
	{
		myPlayer.playSound(loc, sound, volume, pitch);
	}

	@Override
	public void playSound(Location loc, Sound sound, SoundCategory category, float volume, float pitch)
	{
		myPlayer.playSound(loc, sound, category, volume, pitch);
	}

	@Override
	public void playSound(Location loc, String sound, SoundCategory category, float volume, float pitch)
	{
		myPlayer.playSound(loc, sound, category, volume, pitch);
	}

	@Override
	public void removeAchievement(Achievement achievement)
	{
		myPlayer.removeAchievement(achievement);
	}

	@Override
	public void resetPlayerTime()
	{
		myPlayer.resetPlayerTime();
	}

	@Override
	public void resetPlayerWeather()
	{
		myPlayer.resetPlayerWeather();
	}

	@Override
	public void resetTitle()
	{
		myPlayer.resetTitle();
	}

	@Override
	public void saveData()
	{
		myPlayer.saveData();
	}

	@Deprecated
	@Override
	public void sendBlockChange(Location loc, Material type, byte data)
	{
		myPlayer.sendBlockChange(loc, type, data);
	}

	@Deprecated
	@Override
	public void sendBlockChange(Location loc, int type, byte data)
	{
		myPlayer.sendBlockChange(loc, type, data);
	}

	@Deprecated
	@Override
	public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data)
	{
		return myPlayer.sendChunkChange(loc, sx, sy, sz, data);
	}

	@Override
	public void sendMap(MapView map)
	{
		myPlayer.sendMap(map);
	}

	@Override
	public void sendRawMessage(String raw)
	{
		myPlayer.sendRawMessage(raw);
	}

	@Override
	public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException
	{
		myPlayer.sendSignChange(loc, lines);
	}

	@Deprecated
	@Override
	public void sendTitle(String title, String subtitle)
	{
		myPlayer.sendTitle(title, subtitle);
	}

	@Override
	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut)
	{
		myPlayer.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}

	@Override
	public void setAllowFlight(boolean allow)
	{
		myPlayer.setAllowFlight(allow);
	}

	@Override
	public void setBedSpawnLocation(Location loc)
	{
		myPlayer.setBedSpawnLocation(loc);
	}

	@Override
	public void setBedSpawnLocation(Location loc, boolean force)
	{
		myPlayer.setBedSpawnLocation(loc, force);
	}

	@Override
	public void setCompassTarget(Location loc)
	{
		myPlayer.setCompassTarget(loc);
	}

	@Override
	public void setDisplayName(String name)
	{
		myPlayer.setDisplayName(name);
	}

	@Override
	public void setExhaustion(float exhaustion)
	{
		myPlayer.setExhaustion(exhaustion);
	}

	@Override
	public void setFlySpeed(float speed) throws IllegalArgumentException
	{
		myPlayer.setFlySpeed(speed);
	}

	@Override
	public void setFlying(boolean flying)
	{
		myPlayer.setFlying(flying);
	}

	@Override
	public void setPlayerListName(String name)
	{
		myPlayer.setPlayerListName(name);
	}

	@Override
	public void setPlayerTime(long time, boolean relative)
	{
		myPlayer.setPlayerTime(time, relative);
	}

	@Override
	public void setPlayerWeather(WeatherType weather)
	{
		myPlayer.setPlayerWeather(weather);
	}

	@Override
	public void setResourcePack(String url)
	{
		myPlayer.setResourcePack(url);
	}

	@Override
	public void setResourcePack(String url, byte[] hash)
	{
		myPlayer.setResourcePack(url, hash);
	}

	@Override
	public void setSaturation(float saturation)
	{
		myPlayer.setSaturation(saturation);
	}

	@Override
	public void setScoreboard(Scoreboard board) throws IllegalArgumentException, IllegalStateException
	{
		myPlayer.setScoreboard(board);
	}

	@Override
	public void setSleepingIgnored(boolean ignore)
	{
		myPlayer.setSleepingIgnored(ignore);
	}

	@Override
	public void setSneaking(boolean sneaking)
	{
		myPlayer.setSneaking(sneaking);
	}

	@Override
	public void setSpectatorTarget(Entity target)
	{
		myPlayer.setSpectatorTarget(target);
	}

	@Override
	public void setSprinting(boolean sprinting)
	{
		myPlayer.setSprinting(sprinting);
	}

	@Override
	public void setStatistic(Statistic statistic, int amount) throws IllegalArgumentException
	{
		myPlayer.setStatistic(statistic, amount);
	}

	@Override
	public void setStatistic(Statistic statistic, Material type, int amount) throws IllegalArgumentException
	{
		myPlayer.setStatistic(statistic, type, amount);
	}

	@Override
	public void setStatistic(Statistic statistic, EntityType type, int amount)
	{
		myPlayer.setStatistic(statistic, type, amount);
	}

	@Deprecated
	@Override
	public void setTexturePack(String url)
	{
		myPlayer.setTexturePack(url);
	}

	@Override
	public void showPlayer(Player p)
	{
		myPlayer.showPlayer(p);
	}

	@Override
	public void spawnParticle(Particle particle, Location loc, int count)
	{
		myPlayer.spawnParticle(particle, loc, count);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location loc, int count, T data)
	{
		myPlayer.spawnParticle(particle, loc, count, data);
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count)
	{
		myPlayer.spawnParticle(particle, x, y, z, count);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data)
	{
		myPlayer.spawnParticle(particle, x, y, z, count, data);
	}

	@Override
	public void spawnParticle(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ)
	{
		myPlayer.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ, T data)
	{
		myPlayer.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, data);
	}

	@Override
	public void spawnParticle(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ, double extra)
	{
		myPlayer.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, extra);
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ)
	{
		myPlayer.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ, double extra, T data)
	{
		myPlayer.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, extra, data);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data)
	{
		myPlayer.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, data);
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra)
	{
		myPlayer.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data)
	{
		myPlayer.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
	}

	@Override
	public Player.Spigot spigot()
	{
		return myPlayer.spigot();
	}

	@Override
	public void stopSound(Sound sound)
	{
		myPlayer.stopSound(sound);
	}

	@Override
	public void stopSound(String sound)
	{
		myPlayer.stopSound(sound);
	}

	@Override
	public void stopSound(Sound sound, SoundCategory category)
	{
		myPlayer.stopSound(sound, category);
	}

	@Override
	public void stopSound(String sound, SoundCategory category)
	{
		myPlayer.stopSound(sound, category);
	}

	@Override
	public void updateInventory()
	{
		myPlayer.updateInventory();
	}

	@Override
	public Player getPlayer()
	{
		return myPlayer.getPlayer();
	}
}
