<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.tp7.cart.model.Product" %>
<%
  List<Product> items = (List<Product>) request.getAttribute("items");
  if (items == null) items = Collections.emptyList();
  String message = (String) request.getAttribute("message");
  String error = (String) request.getAttribute("error");
%>
<!doctype html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>TP7 – Panier</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
  :root { --bg:#0b1020; --card:#131a2a; --txt:#e7eaf3; --muted:#9aa3b2; --acc:#6aa6ff; --danger:#ff6a6a; }
  body { background:var(--bg); color:var(--txt); font:16px/1.5 system-ui,Segoe UI,Arial; margin:0; }
  .container { max-width:900px; margin:40px auto; padding:0 16px; }
  .grid { display:grid; grid-template-columns:1fr 1fr; gap:20px; }
  .card { background:var(--card); border-radius:16px; padding:20px; box-shadow:0 10px 30px rgba(0,0,0,.35); }
  label { display:block; margin:.4rem 0 .25rem; color:var(--muted); }
  input, textarea { width:100%; padding:.6rem .8rem; background:#0f1525; color:var(--txt);
    border:1px solid #1f2940; border-radius:10px; outline:none; }
  textarea{ min-height:94px; resize:vertical; }
  .btn { display:inline-block; padding:.6rem 1rem; border-radius:10px; border:1px solid #28334f; background:#11192b; color:var(--txt); cursor:pointer; text-decoration:none;}
  .btn:hover { border-color:#3d4d79; }
  .btn-primary { background:linear-gradient(180deg,#1c5fff,#0f47cc); border:none; }
  .btn-danger { background:#2a0f15; border:1px solid #5b1d29; color:#ffd6d6; }
  table { width:100%; border-collapse:collapse; margin-top:10px; }
  th, td { padding:10px; border-bottom:1px solid #223055; }
  .banner { margin:12px 0; padding:10px 12px; border-radius:10px; }
  .ok { background:#0f2b18; border:1px solid #1e5b39; color:#bff3d0; }
  .err{ background:#2b1010; border:1px solid #5b1e1e; color:#ffd0d0; }
</style>
</head>
<body>
<div class="container">
  <h1>TP7 – Panier</h1>

  <% if (message != null) { %><div class="banner ok"><%= message %></div><% } %>
  <% if (error != null)   { %><div class="banner err"><%= error %></div><% } %>

  <div class="grid">
    <div class="card">
      <h2>Ajouter un produit</h2>
      <form method="post" action="cart">
        <input type="hidden" name="action" value="add"/>
        <label for="code">Code (unique)</label>
        <input id="code" name="code" required maxlength="50"/>

        <label for="label">Libellé (max 50)</label>
        <input id="label" name="label" required maxlength="50"/>

        <label for="description">Description</label>
        <textarea id="description" name="description"></textarea>

        <p style="margin-top:10px">
          <button class="btn btn-primary" type="submit">Ajouter au panier</button>
        </p>
      </form>
    </div>

    <div class="card">
      <h2>Votre panier</h2>
      <table>
        <thead><tr><th>Code</th><th>Libellé</th><th>Description</th><th></th></tr></thead>
        <tbody>
        <% if (items.isEmpty()) { %>
          <tr><td colspan="4" style="color:var(--muted)">Aucun produit pour le moment.</td></tr>
        <% } else {
           for (Product p : items) { %>
            <tr>
              <td><%= p.getCode() %></td>
              <td><%= p.getLabel() %></td>
              <td><%= p.getDescription() == null ? "" : p.getDescription() %></td>
              <td>
                <form method="post" action="cart" style="display:inline">
                  <input type="hidden" name="action" value="remove"/>
                  <input type="hidden" name="code" value="<%= p.getCode() %>"/>
                  <button class="btn btn-danger" type="submit">Supprimer</button>
                </form>
              </td>
            </tr>
        <% } } %>
        </tbody>
      </table>

      <form method="post" action="cart" style="margin-top:12px">
        <input type="hidden" name="action" value="checkout"/>
        <button class="btn" type="submit">Finaliser la commande</button>
      </form>
    </div>
  </div>
</div>
</body>
</html>
