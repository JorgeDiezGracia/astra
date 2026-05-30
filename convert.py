 import xml.etree.ElementTree as ET

 tree = ET.parse('assets/atlas/sheet.xml')
 root = tree.getroot()

 lines = []
 lines.append('sheet.png')
 lines.append('size: 1024,1024')
 lines.append('format: RGBA8888')
 lines.append('filter: Linear,Linear')
 lines.append('repeat: none')

 for sub in root.findall('SubTexture'):
     name = sub.get('name')
     x = sub.get('x')
     y = sub.get('y')
     w = sub.get('width')
     h = sub.get('height')
     lines.append(name)
     lines.append('  rotate: false')
     lines.append('  xy: ' + x + ', ' + y)
     lines.append('  size: ' + w + ', ' + h)
     lines.append('  orig: ' + w + ', ' + h)
     lines.append('  offset: 0, 0')
     lines.append('  index: -1')

 with open('assets/atlas/astra.atlas', 'w') as f:
     f.write('\n'.join(lines))

 print('Done! astra.atlas created.')
