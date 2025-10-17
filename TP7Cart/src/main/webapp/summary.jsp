<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.tp7.cart.model.Product" %>
<%
  List<Product> items = (List<Product>) request.getAttribute("items");
  if (items == null) items = Collections.emptyList();
%>
<!doctype html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Récapitulatif de commande</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
  body{background:#0b1020;color:#e7eaf3;font:16px/1.5 system-ui,Segoe UI,Arial;margin:0;}
  .container{max-width:900px;margin:40px auto;padding:0 16px;}
  .card{background:#131a2a;border-radius:16px;padding:20px;box-shadow:0 10px 30px rgba(0,0,0,.35);}
  table{width:100%;border-collapse:collapse;margin-top:10px;}
  th,td{padding:10px;border-bottom:1px solid #223055;}
  a.btn{display:inline-block;margin-top:12px;color:#d0dcff;text-decoration:none;border:1px solid #2b3a60;padding:.5rem .8rem;border-radius:10px;}
</style>
</head>
<body>
<div class="container">
  <div class="card">
    <h1>✅ Commande enregistrée</h1>
    <p>Les produits ci-dessous ont été persistés en base.</p>
    <table>
      <thead><tr><th>Code</th><th>Libellé</th><th>Description</th></tr></thead>
      <tbody>
      <% if (items.isEmpty()) { %>
        <tr><td colspan="3">Aucun article enregistré.</td></tr>
      <% } else { for (Product p : items) { %>
        <tr>
          <td><%= p.getCode() %></td>
          <td><%= p.getLabel() %></td>
          <td><%= p.getDescription() == null ? "" : p.getDescription() %></td>
        </tr>
      <% }} %>
      </tbody>
    </table>
    <a class="btn" href="cart">Retour au panier</a>
  </div>
</div>
</body>
</html>
