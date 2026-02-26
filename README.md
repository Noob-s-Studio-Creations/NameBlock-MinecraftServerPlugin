# **NameBlock – Minecraft Server Plugin**

Hello And Welcome To Another Cool Minecraft Server Management Plugin Called **"NameBlock"**

## **NOTE:** This Plugin Need [Floodgate](https://modrinth.com/mod/floodgate) For Some Features!

## What Is This Plugin Do?

**NameBlock Allows You To Block Specific Parts Of Player Usernames.**

### Example:

You Want To Block The Word:

```
Steve
```

Just Add It Into Your `config.yml`

Now:

* `Steve`
* `Steve123`
* `ProSteveYT`

❌ Cannot Join The Server
They Will Be Automatically Kicked.

---

## 🔎 Regex Support

Want More Advanced Filtering?

You Can Use **Regular Expressions (Regex)** To Create Powerful Name Rules.

Example:

```
^BR_.*
```

This Blocks Any Name That Starts With `BR_`

Perfect For Advanced Server Security 👀

---

## 🌊 Floodgate Support

Fully Compatible With:

* Floodgate
* GeyserMC

---

### Why Is This Important?

If You Run A Crossplay Server (Java + Bedrock):

Floodgate Adds A Prefix To Bedrock Players.

Example:

Prefix = `BR_`

Bedrock Player:

```
Notch
```

Server Sees:

```
BR_Notch
```

---

### The Problem 😬

Java Players Can Also Set Their Username To:

```
BR_Notch
```

If Your Server Is In Offline Mode…

💥 BOOM — PlayerData Conflict
Wrong UUID
Broken Inventory
Big Headache

---

## 🛡 Floodgate Bypass System

With NameBlock You Can Enable:

> **Floodgate Filter Bypass**

That Means:

| Player Type    | Name         | Result    |
| -------------- | ------------ | --------- |
| Java Player    | `BR_Example` | ❌ Blocked |
| Bedrock Player | `BR_Example` | ✅ Allowed |

Because The Plugin Detects Real Floodgate Players.

No More Fake Prefix Abuse 🔥

---

## 📦 Features In V1

* Username Word Blocking
* Regex Support
* Floodgate Smart Bypass
* Lightweight & Simple
* Paper 1.21+ Compatible

Works Great On:

* Paper
* Spigot

---

## 🗓 Version

**V1.0.2**

Last Updated: 26/02/2026 | © Noob's Studio Creations!