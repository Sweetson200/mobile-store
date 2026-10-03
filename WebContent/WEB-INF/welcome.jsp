<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map" %>
<%!
	private String escapeHtml(String value) {
		return value.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;");
	}
%>
<%
	List<Map<String, String>> mobiles = (List<Map<String, String>>) request.getAttribute("mobiles");
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="theme-color" content="#f4f6f1">
	<title>Choose a phone | Current Mobile</title>
	<style>
		:root { color-scheme: light; --ink: #172522; --muted: #68746f; --paper: #f4f6f1; --green: #285c4c; --green-dark: #1c4438; --lime: #d1ed7c; --line: #dce3dc; --coral: #e7785b; }
		* { box-sizing: border-box; }
		body { margin: 0; min-height: 100vh; background: var(--paper); color: var(--ink); font-family: "Aptos", "Segoe UI", sans-serif; }
		header { display: flex; align-items: center; justify-content: space-between; gap: 20px; padding: 20px clamp(20px, 5vw, 72px); background: #fff; border-bottom: 1px solid var(--line); }
		.brand { display: inline-flex; align-items: center; gap: 10px; color: var(--ink); font-size: 17px; font-weight: 750; text-decoration: none; }
		.mark { display: grid; width: 34px; height: 34px; place-items: center; background: var(--green); color: var(--lime); }
		.header-link { color: var(--green); font-size: 13px; font-weight: 700; text-decoration: none; }
		main { width: min(1180px, 100%); margin: 0 auto; padding: 48px clamp(20px, 5vw, 48px) 64px; }
		.intro { display: flex; align-items: end; justify-content: space-between; gap: 24px; margin-bottom: 26px; }
		.kicker { margin: 0 0 10px; color: var(--green); font-size: 11px; font-weight: 750; letter-spacing: 1.4px; text-transform: uppercase; }
		h1 { margin: 0; font-family: Georgia, "Times New Roman", serif; font-size: clamp(36px, 5vw, 52px); font-weight: 500; line-height: 1.05; }
		.subtitle { margin: 12px 0 0; color: var(--muted); font-size: 14px; line-height: 1.5; }
		.selection-count { color: var(--muted); font-size: 13px; white-space: nowrap; }
		.mobile-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(min(100%, 285px), 1fr)); gap: 14px; }
		.mobile-option { position: relative; display: block; min-width: 0; }
		.mobile-option input { position: absolute; top: 17px; right: 17px; z-index: 1; width: 20px; height: 20px; margin: 0; accent-color: var(--green); cursor: pointer; }
		.mobile-option input:disabled { cursor: not-allowed; }
		.mobile-card { height: 100%; min-height: 330px; border: 1px solid var(--line); background: #fff; transition: border-color 150ms ease, box-shadow 150ms ease; }
		.mobile-option input:checked + .mobile-card { border-color: var(--green); box-shadow: inset 0 0 0 1px var(--green); }
		.mobile-option input:focus-visible + .mobile-card { outline: 3px solid rgb(40 92 76 / 25%); outline-offset: 2px; }
		.product-image { display: grid; height: 180px; place-items: center; overflow: hidden; background: #edf1eb; }
		.product-image img { width: 100%; height: 100%; object-fit: contain; }
		.image-fallback { color: var(--green); font-family: Georgia, "Times New Roman", serif; font-size: 24px; }
		.product-info { padding: 18px; }
		.product-heading { display: flex; justify-content: space-between; align-items: start; gap: 12px; margin-bottom: 15px; }
		.mobile-card h2 { margin: 0; font-size: 16px; font-weight: 750; line-height: 1.3; }
		.stock-state { flex: 0 0 auto; color: var(--green); font-size: 11px; font-weight: 700; }
		.stock-state.out-of-stock { color: #9b4937; }
		dl { display: grid; grid-template-columns: minmax(75px, .7fr) minmax(0, 1.3fr); gap: 8px 12px; margin: 0; font-size: 12px; line-height: 1.45; }
		dt { color: var(--muted); overflow-wrap: anywhere; }
		dd { min-width: 0; margin: 0; color: var(--ink); overflow-wrap: anywhere; }
		.price { color: var(--green-dark); font-size: 16px; font-weight: 750; }
		.description { margin: 14px 0 0; color: var(--muted); font-size: 12px; line-height: 1.5; }
		.empty { padding: 36px 20px; border: 1px solid var(--line); background: #fff; color: var(--muted); text-align: center; }
		.checkout-bar { position: sticky; bottom: 0; display: flex; align-items: center; justify-content: space-between; gap: 18px; margin-top: 22px; padding: 14px 16px; border: 1px solid var(--line); background: rgb(255 255 255 / 96%); }
		.checkout-bar p { margin: 0; color: var(--muted); font-size: 13px; }
		button { min-height: 46px; border: 0; border-radius: 2px; background: var(--green); color: white; cursor: pointer; font: inherit; font-size: 13px; font-weight: 700; padding: 0 16px; }
		button:hover:not(:disabled) { background: var(--green-dark); }
		button:disabled { background: #9ba7a0; cursor: not-allowed; }
		.status { min-height: 20px; margin: 12px 0 0; color: var(--green); font-size: 13px; }
		@media (max-width: 580px) { header { padding: 14px 18px; } main { padding-top: 32px; } .intro { display: block; } .selection-count { display: block; margin-top: 15px; } .checkout-bar { align-items: stretch; flex-direction: column; } button { width: 100%; } }
	</style>
</head>
<body>
	<header>
		<a class="brand" href="login.html" aria-label="Current Mobile home"><span class="mark" aria-hidden="true">C</span><span>current<span style="color: var(--green)">.</span></span></a>
		<a class="header-link" href="login.html">Sign out</a>
	</header>
	<main>
		<section class="intro" aria-labelledby="page-title">
			<div>
				<p class="kicker">Current Mobile</p>
				<h1 id="page-title">Find your next phone.</h1>
				<p class="subtitle">Select the phones you want to take to checkout.</p>
			</div>
			<span class="selection-count" id="selection-count" aria-live="polite">0 selected</span>
		</section>

		<% if (mobiles == null || mobiles.isEmpty()) { %>
			<p class="empty">No mobile phones are available right now.</p>
		<% } else { %>
			<div class="mobile-grid" id="mobile-grid">
				<% for (Map<String, String> mobile : mobiles) {
					int stockQuantity = Integer.parseInt(mobile.get("stockQuantity"));
					boolean inStock = stockQuantity > 0;
					String title = mobile.get("brand") + " " + mobile.get("modelName");
					String imageUrl = mobile.get("imageUrl");
				%>
					<label class="mobile-option">
						<input type="checkbox" name="selectedMobiles" value="<%= escapeHtml(mobile.get("mobileId")) %>" data-price="<%= escapeHtml(mobile.get("price")) %>" aria-label="Select <%= escapeHtml(title) %> for checkout" <%= inStock ? "" : "disabled" %>>
						<article class="mobile-card">
							<div class="product-image">
								<% if (!imageUrl.isEmpty()) { %>
									<img src="<%= escapeHtml(imageUrl) %>" alt="<%= escapeHtml(title) %>">
								<% } else { %>
									<span class="image-fallback" aria-hidden="true">current.</span>
								<% } %>
							</div>
							<div class="product-info">
								<div class="product-heading">
									<h2><%= escapeHtml(title) %></h2>
									<span class="stock-state <%= inStock ? "" : "out-of-stock" %>"><%= inStock ? stockQuantity + " in stock" : "Out of stock" %></span>
								</div>
								<dl>
									<dt>Color</dt><dd><%= escapeHtml(mobile.get("color")) %></dd>
									<dt>Storage</dt><dd><%= escapeHtml(mobile.get("storageGb")) %> GB</dd>
									<dt>Memory</dt><dd><%= escapeHtml(mobile.get("ramGb")) %> GB RAM</dd>
									<dt>Price</dt><dd class="price"><%= escapeHtml(mobile.get("price")) %></dd>
								</dl>
								<% if (mobile.get("description") != null && !mobile.get("description").isBlank()) { %>
									<p class="description"><%= escapeHtml(mobile.get("description")) %></p>
								<% } %>
							</div>
						</article>
					</label>
				<% } %>
			</div>
		<% } %>

		<div class="checkout-bar">
			<p>Checkout is ready for your selection.</p>
			<button id="checkout-button" type="button" disabled>Continue to checkout</button>
		</div>
		<p class="status" id="selection-status" role="status" aria-live="polite"></p>
	</main>
	<script>
		const choices = Array.from(document.querySelectorAll('input[name="selectedMobiles"]'));
		const count = document.querySelector('#selection-count');
		const checkoutButton = document.querySelector('#checkout-button');
		const status = document.querySelector('#selection-status');

		function updateSelection() {
			const selected = choices.filter((choice) => choice.checked);
			const total = selected.reduce((sum, choice) => sum + Number(choice.dataset.price), 0);
			count.textContent = selected.length + ' selected · Total ' + total.toFixed(2);
			checkoutButton.disabled = selected.length === 0;
			status.textContent = '';
		}

		choices.forEach((choice) => choice.addEventListener('change', updateSelection));
		checkoutButton.addEventListener('click', () => {
			const selectedCount = choices.filter((choice) => choice.checked).length;
			status.textContent = selectedCount + ' mobile' + (selectedCount === 1 ? '' : 's')
					+ ' selected. Order checkout is not connected yet.';
		});
	</script>
</body>
</html>
